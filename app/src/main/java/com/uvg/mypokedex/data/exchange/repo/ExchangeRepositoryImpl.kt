package com.uvg.mypokedex.data.exchange.repo

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uvg.mypokedex.domain.model.Exchange
import com.uvg.mypokedex.domain.repo.ExchangeRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class ExchangeRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
): ExchangeRepository {
    private fun uid(): String = requireNotNull(auth.currentUser?.uid) { "Not signed in" }
    private fun exchanges() = db.collection("exchanges")
    private fun userFavs(uid: String) = db.collection("users").document(uid).collection("favorites")

    // Generar código random de intercambio
    private fun genCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        return (1..6).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }

    // Usuario A selecciona un pokemon y crea el Exchange
    override suspend fun createExchangeWithOfferA(offerAId: Int, offerAName: String): Result<Exchange> = runCatching {
        val code = genCode()
        val doc = exchanges().document()
        val data = mapOf(
            "code" to code,
            "userA" to uid(),
            "userB" to null,
            "offerA" to mapOf("id" to offerAId, "name" to offerAName),
            "offerB" to null,
            "status" to Exchange.Status.PROPOSED.name,
            "createdAt" to Timestamp.now()
        )
        doc.set(data).await()
        Exchange(
            id = doc.id, code = code, userA = uid(), userB = null,
            offerAId = offerAId, offerAName = offerAName,
            offerBId = null, offerBName = null,
            status = Exchange.Status.PROPOSED
        )
    }

    // Usuario B se une al Exchange con el código
    override suspend fun joinByCode(code: String): Result<Exchange> = runCatching {
        // Busca exchange por code
        val snap = exchanges().whereEqualTo("code", code).limit(1).get().await()
        val doc = snap.documents.firstOrNull() ?: error("Code not found")
        val data = doc.data ?: error("Invalid exchange")
        val userA = data["userA"] as String
        val currentUid = uid()
        if (currentUid == userA) error("You cannot join your own exchange")

        // Si ya hay userB, seguimos, si no lo seteamos
        if ((data["userB"] as? String) == null) {
            doc.reference.update(
                mapOf(
                    "userB" to currentUid,
                    "status" to Exchange.Status.JOINED.name
                )
            ).await()
        }

        // Devuelve Exchange
        val offerA = data["offerA"] as Map<*, *>
        Exchange(
            id = doc.id,
            code = data["code"] as String,
            userA = userA,
            userB = currentUid,
            offerAId = (offerA["id"] as Number).toInt(),
            offerAName = offerA["name"] as String,
            offerBId = null,
            offerBName = null,
            status = Exchange.Status.JOINED
        )
    }

    // Usuario B finaliza el trade
    override suspend fun commitWithOfferB(exchangeId: String, offerBId: Int, offerBName: String): Result<Unit> = runCatching {
        val currentUid = uid()

        db.runTransaction { trx ->
            val exRef = exchanges().document(exchangeId)
            val exSnap = trx.get(exRef)
            if (!exSnap.exists()) error("Exchange not found")

            val ex = exSnap.data!!
            val userA = ex["userA"] as String
            val userB = (ex["userB"] as? String) ?: currentUid
            val status = Exchange.Status.valueOf(ex["status"] as String)

            if (currentUid != userB) error("Only the joiner can commit")
            if (status == Exchange.Status.COMMITTED) return@runTransaction null
            if (status !in listOf(Exchange.Status.JOINED, Exchange.Status.PROPOSED)) error("Invalid state")

            val offerA = ex["offerA"] as Map<*, *>
            val offerAId = (offerA["id"] as Number).toInt()
            val offerAName = offerA["name"] as String

            // Verifica existencias
            val favARef = userFavs(userA).document(offerAId.toString())
            val favBRef = userFavs(userB).document(offerBId.toString())
            val favASnap = trx.get(favARef)
            val favBSnap = trx.get(favBRef)
            if (!favASnap.exists()) error("A no longer has that favorite")
            if (!favBSnap.exists()) error("B no longer has that favorite")

            // Se swapean los favoritos
            trx.delete(favARef)
            trx.delete(favBRef)

            trx.set(userFavs(userA).document(offerBId.toString()), mapOf(
                "id" to offerBId,
                "name" to offerBName,
                "addedAt" to Timestamp.now()
            ))
            trx.set(userFavs(userB).document(offerAId.toString()), mapOf(
                "id" to offerAId,
                "name" to offerAName,
                "addedAt" to Timestamp.now()
            ))

            // Actualiza exchange
            trx.update(exRef, mapOf(
                "offerB" to mapOf("id" to offerBId, "name" to offerBName),
                "status" to Exchange.Status.COMMITTED.name,
                "committedAt" to Timestamp.now()
            ))
            null
        }.await()
    }

    // Función que observa los cambios en un Exchange
    override fun observeExchange(exchangeId: String): Flow<Exchange?> = callbackFlow {
        val reg = exchanges().document(exchangeId)
            .addSnapshotListener { snap, err ->
                if (err != null || snap == null || !snap.exists()) {
                    trySend(null); return@addSnapshotListener
                }
                val data = snap.data!!
                val offerA = data["offerA"] as Map<*, *>
                val offerB = data["offerB"] as? Map<*, *>
                trySend(
                    Exchange(
                        id = snap.id,
                        code = data["code"] as String,
                        userA = data["userA"] as String,
                        userB = data["userB"] as? String,
                        offerAId = (offerA["id"] as Number).toInt(),
                        offerAName = offerA["name"] as String,
                        offerBId = (offerB?.get("id") as? Number)?.toInt(),
                        offerBName = offerB?.get("name") as? String,
                        status = Exchange.Status.valueOf(data["status"] as String)
                    )
                )
            }
        awaitClose { reg.remove() }
    }
}
package com.uvg.mypokedex.data.favorites.repo

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.uvg.mypokedex.domain.model.FavoritePokemon
import com.uvg.mypokedex.domain.repo.AuthRepository
import com.uvg.mypokedex.domain.repo.FavoritesRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FavoritesRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val authRepo: AuthRepository
): FavoritesRepository {
    // Revisa si el usuario realmente está autenticado
    private fun uidOrThrow(): String {
        require(authRepo.isSignedIn()) { "Not signed in" }
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    // Es la "ruta" o referencia a los favoritos del usuario
    private fun favsRef() = firestore
        .collection("users")
        .document(uidOrThrow())
        .collection("favorites")

    // Función que devuelve un flow con la lista de favoritos del usuario guardada en firebase
    override fun observeFavorites(): Flow<List<FavoritePokemon>> = callbackFlow {
        val reg = favsRef()
            .orderBy("addedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val items = snap?.documents?.mapNotNull { doc ->
                    val id = (doc.getLong("id") ?: return@mapNotNull null).toInt()
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val addedAt = (doc.getTimestamp("addedAt") ?: Timestamp.now()).toDate().time
                    FavoritePokemon(id = id, name = name, addedAt = addedAt)
                }.orEmpty()
                trySend(items)
            }
        awaitClose { reg.remove() }
    }

    // Revisa si el pokemon está en la lista de favoritos
    override fun observeIsFavorite(pokemonId: Int): Flow<Boolean> =
        observeFavorites().map { list -> list.any { it.id == pokemonId } }

    // Agrega un pokemon a la lista de favoritos
    override suspend fun addFavorite(pokemonId: Int, name: String): Result<Unit> = runCatching {
        val data = mapOf(
            "id" to pokemonId,
            "name" to name,
            "addedAt" to Timestamp.now()
        )
        favsRef().document(pokemonId.toString()).set(data).await()
    }

    // Borra un pokemon de la lista de favoritos
    override suspend fun removeFavorite(pokemonId: Int): Result<Unit> = runCatching {
        favsRef().document(pokemonId.toString()).delete().await()
    }
}
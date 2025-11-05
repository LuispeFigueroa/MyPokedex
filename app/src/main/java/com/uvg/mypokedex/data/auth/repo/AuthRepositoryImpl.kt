package com.uvg.mypokedex.data.auth.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uvg.mypokedex.domain.repo.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
): AuthRepository {
    // Flow que contiene el usuario actual
    override val currentUserFlow: Flow<FirebaseUser?> =
        callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }.distinctUntilChanged()

    // Devuele true si el usuario ya se autenticó, false si no
    override fun isSignedIn(): Boolean = auth.currentUser != null

    // Hace un sign in anónimo y devuelve el usuario si es exitoso
    override suspend fun signInAnonymously(): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUser ?: auth.signInAnonymously().await().user
            Result.success(requireNotNull(user))
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}
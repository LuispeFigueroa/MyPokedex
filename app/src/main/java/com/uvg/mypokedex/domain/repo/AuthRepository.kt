package com.uvg.mypokedex.domain.repo

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserFlow: Flow<FirebaseUser?>
    fun isSignedIn(): Boolean
    suspend fun signInAnonymously(): Result<FirebaseUser>
}
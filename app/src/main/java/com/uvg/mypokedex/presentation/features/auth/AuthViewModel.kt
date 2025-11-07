package com.uvg.mypokedex.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.core.common.RepositoryProvider
import com.uvg.mypokedex.domain.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = RepositoryProvider.provideAuthRepository()
): ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun isSignedIn(): Boolean = repo.isSignedIn()

    fun signInAnonymously() {
        if (repo.isSignedIn()) return
        viewModelScope.launch {
            _state.update { it.copy(isSigningIn = true, error = null) }
            val r = repo.signInAnonymously()
            _state.update { s ->
                r.fold(
                    onSuccess = { s.copy(isSigningIn = false, userId = it.uid) },
                    onFailure = { s.copy(isSigningIn = false, error = "Unable to sign in") }
                )
            }
        }
    }

    fun clearError() = _state.update { it.copy(error = null) }
}
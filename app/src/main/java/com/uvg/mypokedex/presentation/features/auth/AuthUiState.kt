package com.uvg.mypokedex.presentation.features.auth

data class AuthUiState(
    val isSigningIn: Boolean = false,
    val error: String? = null,
    val userId: String? = null
)
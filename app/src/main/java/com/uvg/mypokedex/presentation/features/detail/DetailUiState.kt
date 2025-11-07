package com.uvg.mypokedex.presentation.features.detail

import com.uvg.mypokedex.domain.common.AppError
import com.uvg.mypokedex.domain.model.Pokemon

data class DetailUiState(
    val isLoading: Boolean = true,
    val pokemon: Pokemon? = null,
    val error: AppError? = null,
    val isFavorite: Boolean = false
)
package com.uvg.mypokedex.presentation.features.detail

import com.uvg.mypokedex.domain2.common.AppError
import com.uvg.mypokedex.domain2.model.Pokemon

data class DetailUiState(
    val isLoading: Boolean = true,
    val pokemon: Pokemon? = null,
    val error: AppError? = null,
    val isFavorite: Boolean = false
)
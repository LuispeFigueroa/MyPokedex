package com.uvg.mypokedex.domain.common

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    object Empty : UiState<Nothing>()
    data class Error(val error: AppError) : UiState<Nothing>()
    data class Content<T>(val data: T) : UiState<T>()
}
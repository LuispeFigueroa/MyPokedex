package com.uvg.mypokedex.presentation.features.detail

sealed interface DetailEvent {
    data object RequireLogin : DetailEvent
}
package com.uvg.mypokedex.presentation.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.domain.model.FavoritePokemon
import com.uvg.mypokedex.domain.repo.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TradeSelectViewModel(
    private val repo: FavoritesRepository
): ViewModel() {
    val favorites: StateFlow<List<FavoritePokemon>> =
        repo.observeFavorites()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
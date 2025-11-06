package com.uvg.mypokedex.presentation.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.domain.model.FavoritePokemon
import com.uvg.mypokedex.domain.repo.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class TradeSelectViewModel(
    favoritesRepo: FavoritesRepository
): ViewModel() {
    val favorites: StateFlow<List<FavoritePokemon>> =
        favoritesRepo.observeFavorites()
            .onEach { list ->
                android.util.Log.d("TradeSelectVM", "observeFavorites emitted size=${list.size}, values=$list")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
}
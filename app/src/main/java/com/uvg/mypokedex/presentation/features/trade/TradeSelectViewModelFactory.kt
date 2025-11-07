package com.uvg.mypokedex.presentation.features.trade

import androidx.lifecycle.ViewModelProvider
import com.uvg.mypokedex.domain.repo.FavoritesRepository

@Suppress("UNCHECKED_CAST")
class TradeSelectViewModelFactory(
    private val faveRepo: FavoritesRepository
): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return TradeSelectViewModel(
            faveRepo,
        ) as T
    }
}
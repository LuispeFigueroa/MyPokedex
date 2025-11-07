package com.uvg.mypokedex.presentation.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uvg.mypokedex.domain.repo.ExchangeRepository
import com.uvg.mypokedex.domain.repo.FavoritesRepository

@Suppress("UNCHECKED_CAST")
class TradeViewModelFactory(
    private val exchangeRepo: ExchangeRepository
): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return TradeViewModel(
            exchangeRepo,
        ) as T
    }
}
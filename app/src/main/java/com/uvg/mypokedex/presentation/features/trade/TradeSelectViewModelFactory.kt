package com.uvg.mypokedex.presentation.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uvg.mypokedex.domain.repo.FavoritesRepository

class TradeSelectViewModelFactory(
    private val repo: FavoritesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(TradeSelectViewModel::class.java))
        return TradeSelectViewModel(repo) as T
    }
}
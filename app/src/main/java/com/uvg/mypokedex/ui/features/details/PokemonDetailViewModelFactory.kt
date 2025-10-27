package com.uvg.mypokedex.ui.features.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uvg.mypokedex.data.repository.RepositoryProvider

class PokemonDetailViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = RepositoryProvider.providePokemonRepository(application)
        return PokemonDetailViewModel(repo) as T
    }
}
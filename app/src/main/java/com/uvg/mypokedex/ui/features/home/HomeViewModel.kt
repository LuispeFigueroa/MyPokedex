package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.core.common.Result
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem
import com.uvg.mypokedex.data.repository.PokemonRepository
import com.uvg.mypokedex.data.repository.RepositoryProvider
import com.uvg.mypokedex.ui.state.PokedexListUiState
import com.uvg.mypokedex.ui.state.SortField
import com.uvg.mypokedex.ui.state.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val repo: PokemonRepository = RepositoryProvider.pokemonRepository
) : AndroidViewModel(application) {

    val pokemonList = mutableStateListOf<PokemonListItem>()
    private val _uiState = MutableStateFlow(PokedexListUiState())
    val uiState: StateFlow<PokedexListUiState> = _uiState

    private var hasMore = true

    fun loadMorePokemon() {
        if (_uiState.value.isLoading || !hasMore) return

        viewModelScope.launch {
            repo.loadNextPage(pokemonList.size).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Result.Success -> {
                        val pageItems = result.data
                        if (pageItems.isEmpty()) {
                            hasMore = false
                        }
                        pokemonList.addAll(pageItems)
                        _uiState.update {
                            it.copy(isLoading = false, canLoadMore = pageItems.isNotEmpty())
                        }
                    }
                    is Result.Error -> {

                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onSortFieldChanged(field: SortField) {
        _uiState.update { it.copy(sortField = field) }
    }

    fun onSortOrderChanged(order: SortOrder) {
        _uiState.update { it.copy(sortOrder = order) }
    }
}
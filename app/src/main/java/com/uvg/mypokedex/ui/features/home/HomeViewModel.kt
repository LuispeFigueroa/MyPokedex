package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.core.common.Result
// IMPORTANTE: Asegúrate que PokemonListItem esté importado
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem
import com.uvg.mypokedex.data.repository.PokemonRepository
import com.uvg.mypokedex.data.repository.RepositoryProvider
import com.uvg.mypokedex.ui.state.PokedexListUiState
import com.uvg.mypokedex.ui.state.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val repo: PokemonRepository = RepositoryProvider.pokemonRepository
) : AndroidViewModel(application) {

    // PASO 1: La lista ahora almacena el objeto simple PokemonListItem
    val pokemonList = mutableStateListOf<PokemonListItem>()

    private val _uiState = MutableStateFlow(PokedexListUiState())
    val uiState: StateFlow<PokedexListUiState> = _uiState

    private var isLoading = false
    private var hasMore = true

    // PASO 2: La función de carga se simplifica enormemente
    fun loadMorePokemon() {
        if (isLoading || !hasMore) return
        isLoading = true
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val currentCount = pokemonList.size
            repo.loadNextPage(currentCount).collect { res ->
                when (res) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        val pageItems: List<PokemonListItem> = res.data
                        if (pageItems.isEmpty()) {
                            hasMore = false
                        }

                        // Simplemente agregamos los items de la página a nuestra lista
                        pokemonList.addAll(pageItems)

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                totalLoaded = pokemonList.size,
                                canLoadMore = pageItems.isNotEmpty()
                            )
                        }
                        isLoading = false
                    }
                    is Result.Error -> {
                        isLoading = false
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = res.message)
                        }
                    }
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onSortOrderSelected(order: SortOrder) {
        _uiState.update { it.copy(sortOrder = order) }
    }

    fun toggleSortOrder() {
        val newOrder =
            if (_uiState.value.sortOrder == SortOrder.BY_NAME_ASC) SortOrder.BY_NAME_DESC
            else SortOrder.BY_NAME_ASC
        onSortOrderSelected(newOrder)
    }
}





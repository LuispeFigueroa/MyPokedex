package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.core.common.Result
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem
import com.uvg.mypokedex.data.repository.PokemonRepository
import com.uvg.mypokedex.domain.sort.PokemonOrder
import com.uvg.mypokedex.ui.state.PokedexListUiState
import com.uvg.mypokedex.ui.state.SortField
import com.uvg.mypokedex.ui.state.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val repo: PokemonRepository,
) : AndroidViewModel(application) {

    val pokemonList = mutableStateListOf<PokemonListItem>()
    private val _uiState = MutableStateFlow(PokedexListUiState())
    val uiState: StateFlow<PokedexListUiState> = _uiState

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun loadMorePokemon() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            if (!isConnected.value) {
                _uiState.update { it.copy(errorMessage = "Sin conexión. Mostrando datos locales.") }
                return@launch
            }

            _uiState.update { it.copy(errorMessage = null) }

            repo.fetchAndCacheNextPage(pokemonList.size).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }

                    is Result.Success -> {
                        // No tocamos canLoadMore aquí. Lo actualizamos cuando recibimos cachedPokemon.
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
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

    val currentOrder: StateFlow<PokemonOrder> =
        repo.orderPreference.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            PokemonOrder.NUMBER_ASC
        )

    fun onOrderSelected(newOrder: PokemonOrder) {
        viewModelScope.launch {
            repo.changeOrderPreference(newOrder)
        }
    }

    private fun applyCurrentOrder(order: PokemonOrder) {
        val sorted: List<PokemonListItem> = when (order) {
            PokemonOrder.NUMBER_ASC -> pokemonList.sortedBy { item: PokemonListItem -> item.id }
            PokemonOrder.NUMBER_DESC -> pokemonList.sortedByDescending { item: PokemonListItem -> item.id }
            PokemonOrder.NAME_ASC -> pokemonList.sortedBy { item: PokemonListItem -> item.name }
            PokemonOrder.NAME_DESC -> pokemonList.sortedByDescending { item: PokemonListItem -> item.name }
        }

        pokemonList.clear()
        pokemonList.addAll(sorted)
    }

    init {
        viewModelScope.launch {
            repo.isConnected.collect { connected ->
                _isConnected.value = connected
            }
        }

        viewModelScope.launch {
            currentOrder.collect { order ->
                applyCurrentOrder(order)

                val (newField, newSortOrder) = when (order) {
                    PokemonOrder.NUMBER_ASC -> SortField.BY_NUMBER to SortOrder.ASC
                    PokemonOrder.NUMBER_DESC -> SortField.BY_NUMBER to SortOrder.DESC
                    PokemonOrder.NAME_ASC -> SortField.BY_NAME to SortOrder.ASC
                    PokemonOrder.NAME_DESC -> SortField.BY_NAME to SortOrder.DESC
                }

                _uiState.update {
                    it.copy(
                        sortField = newField,
                        sortOrder = newSortOrder
                    )
                }
            }
        }

        viewModelScope.launch {
            repo.cachedPokemon.collect { listFromDb ->
                pokemonList.clear()
                pokemonList.addAll(listFromDb)

                applyCurrentOrder(currentOrder.value)

                _uiState.update { state ->
                    state.copy(
                        canLoadMore = true,
                        isLoading = false
                    )
                }
            }
        }
    }
}

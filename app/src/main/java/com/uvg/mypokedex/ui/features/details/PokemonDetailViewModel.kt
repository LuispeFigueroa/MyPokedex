package com.uvg.mypokedex.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.core.common.Result
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonDetailUiState(
    val isLoading: Boolean = false,
    val pokemon: Pokemon? = null,
    val errorMessage: String? = null
)

class PokemonDetailViewModel(
    private val repo: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState

    fun load(nameOrId: String) {
        viewModelScope.launch {
            repo.getPokemonDetail(nameOrId).collect { res ->
                when (res) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Result.Success -> _uiState.update { it.copy(isLoading = false, pokemon = res.data) }
                    is Result.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = res.message) }
                }
            }
        }
    }

    fun setPokemon(pokemon: Pokemon) {
        _uiState.update { it.copy(isLoading = false, errorMessage = null, pokemon = pokemon) }
    }
}

package com.uvg.mypokedex.ui.state

import com.uvg.mypokedex.data.model.Pokemon

data class PokemonDetailUiState(
    val isLoading: Boolean = false,
    val pokemon: Pokemon? = null,
    val errorMessage: String? = null
)
package com.uvg.mypokedex.ui.features.details

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonMeasurements
import com.uvg.mypokedex.ui.components.PokemonStatRow

@Composable
fun DetailScreen(
    pokemon: Pokemon
) {
    Column {
        PokemonMeasurements(pokemon = pokemon)
        PokemonStatRow(pokemon = pokemon)
    }
}
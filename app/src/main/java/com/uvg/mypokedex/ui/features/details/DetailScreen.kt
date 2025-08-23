package com.uvg.mypokedex.ui.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonCard
import com.uvg.mypokedex.ui.components.PokemonMeasurements
import com.uvg.mypokedex.ui.components.PokemonStatRow
import com.uvg.mypokedex.ui.components.UnstablePokemonList
import com.uvg.mypokedex.ui.features.home.HomeViewModel

@Composable
fun DetailScreen(
    pokemon: Pokemon
) {
    Column {
        PokemonMeasurements(pokemon = pokemon)
        PokemonStatRow(pokemon = pokemon)
    }
}
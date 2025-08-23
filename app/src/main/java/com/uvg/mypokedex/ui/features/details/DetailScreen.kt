package com.uvg.mypokedex.ui.features.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonMeasurements
import com.uvg.mypokedex.ui.components.PokemonStatRow
import com.uvg.mypokedex.ui.components.TopBar

@Composable
fun DetailScreen(
    pokemon: Pokemon,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                name = pokemon.name,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            PokemonMeasurements(pokemon = pokemon)
            PokemonStatRow(pokemon = pokemon)
        }
    }
}
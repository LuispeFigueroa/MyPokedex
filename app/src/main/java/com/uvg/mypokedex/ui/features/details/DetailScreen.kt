package com.uvg.mypokedex.ui.features.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonMeasurements
import com.uvg.mypokedex.ui.components.PokemonStatRow
import com.uvg.mypokedex.ui.components.StatType
import com.uvg.mypokedex.ui.components.TopBar

@Composable
fun DetailScreen(
    pokemon: Pokemon,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                name = pokemon.name,
                isFavorite = isFavorite,
                onToggleFavorite = onToggleFavorite,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PokemonMeasurements(pokemon = pokemon)
            Spacer(Modifier.height(12.dp))

            for (stat in StatType.entries) {
                PokemonStatRow(
                    pokemon = pokemon,
                    stat = stat,
                )
            }
        }
    }
}
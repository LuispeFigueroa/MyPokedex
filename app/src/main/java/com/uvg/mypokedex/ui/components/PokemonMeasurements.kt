package com.uvg.mypokedex.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.uvg.mypokedex.data.model.Pokemon
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PokemonMeasurements(pokemon: Pokemon) {
    val height = pokemon.height
    val weight = pokemon.weight

    Text(
        text = "height: ${height}\nweight: ${weight}",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )
}
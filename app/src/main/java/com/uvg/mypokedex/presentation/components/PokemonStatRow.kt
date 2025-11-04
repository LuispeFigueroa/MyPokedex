package com.uvg.mypokedex.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.domain2.model.PokemonStat

@Composable
fun PokemonStatRow(stat: PokemonStat) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = statDisplayName(stat.stat.name),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stat.baseStat.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(6.dp))

        // Llamada a la extensión @Composable en PokemonStat:
        stat.StatBar()
    }
}

// Función helper para modificar el nombre de la stat a uno más amigable para UI
private fun statDisplayName(apiName: String): String = when (apiName.lowercase()) {
    "hp" -> "HP"
    "attack" -> "Attack"
    "defense" -> "Defense"
    "special-attack" -> "Special Attack"
    "special-defense" -> "Special Defense"
    "speed" -> "Speed"
    else -> apiName.replaceFirstChar { it.uppercase() }
}
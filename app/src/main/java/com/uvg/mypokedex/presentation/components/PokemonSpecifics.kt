package com.uvg.mypokedex.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.domain.model.PokemonAbility
import com.uvg.mypokedex.domain.model.PokemonType

// Composable para mostrar los tipos y habilidades de un pokemon
@Composable
fun PokemonSpecifics(
    types: List<PokemonType>,
    abilities: List<PokemonAbility>
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Types", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            types.forEach { t ->
                AssistChip(
                    onClick = { /* TODO: Mostrar información del tipo */ },
                    label = { Text(t.type.name.replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Text("Abilities", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            abilities.forEach { a ->
                AssistChip(
                    onClick = { /* TODO: Mostrar información de la habilidad */ },
                    label = { Text(a.ability.name.replaceFirstChar { it.uppercase() }) }
                )
            }
        }
    }
}
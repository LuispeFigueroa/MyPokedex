package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.uvg.mypokedex.data.model.Pokemon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class StatType {Health, Attack, Defense, SpecialAttack, Speed}

@Composable
fun PokemonStatRow(
    pokemon: Pokemon,
    stat: StatType,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        when (stat) {
            StatType.Health -> {
                pokemon.stats.StatBar()
            }
            StatType.Attack         -> StatLine("Attack",         pokemon.stats.Attack)
            StatType.Defense        -> StatLine("Defense",        pokemon.stats.Defense)
            StatType.SpecialAttack  -> StatLine("Special Attack", pokemon.stats.SpecialAttack)
            StatType.Speed          -> StatLine("Speed",          pokemon.stats.Speed)
        }
    }
}

@Composable
private fun StatLine(label: String, value: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Text("$value", style = MaterialTheme.typography.labelLarge)
    }
}
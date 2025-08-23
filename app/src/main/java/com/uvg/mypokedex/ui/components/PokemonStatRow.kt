package com.uvg.mypokedex.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.uvg.mypokedex.data.model.Pokemon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun PokemonStatRow(pokemon: Pokemon, stat: Int) {

    val currentHp = pokemon.health
    val maxHp = pokemon.maxHealth

    val target = (currentHp.toFloat() / maxHp.coerceAtLeast(1)).coerceIn(0f, 1f)
    val progress by animateFloatAsState(targetValue = target, label = "hpProgress")

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HP: $currentHp/$maxHp (${(target * 100).roundToInt()}%)",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.height(6.dp))

        // Material 3: progress es un lambda { 0f..1f }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp)
        )
    }
}
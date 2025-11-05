package com.uvg.mypokedex.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.domain.model.PokemonStat
import com.uvg.mypokedex.presentation.features.detail.StatMax.maxFor

// Función de extensión para mostrar la barra de progreso de una stat
@Composable
fun PokemonStat.StatBar() {
    val max = maxFor(this.stat.name)
    val progress = (this.baseStat.coerceAtMost(max)).toFloat() / max.toFloat()

    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    )
}
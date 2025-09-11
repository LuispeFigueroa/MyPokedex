package com.uvg.mypokedex.ui.components

import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.DefaultTab.AlbumsTab.value
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Stats
import kotlin.math.roundToInt

@Composable
fun Stats.StatBar(
    modifier: Modifier = Modifier,
    showPercent: Boolean = true
) {
    val current = hpCurrent
    val max = hp.coerceAtLeast(1)
    val ratio = (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)
    val progress by animateFloatAsState(targetValue = ratio, label = "hpProgress")

    Column(modifier = modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("HP", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Text(
                text = if (showPercent) "$current/$max (${(ratio * 100).roundToInt()}%)" else "$current/$max",
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress },                 // Material 3: lambda 0f..1f
            modifier = Modifier.fillMaxWidth().height(8.dp)
        )
    }
}
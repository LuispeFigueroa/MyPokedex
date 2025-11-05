package com.uvg.mypokedex.presentation.features.trade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeShowCodeScreen(
    pokemonId: Int,
    pokemonName: String,
    onBack: () -> Unit
) {
    // Placeholder: generamos un código bonito (6 letras/números)
    val code by remember { mutableStateOf("codigo") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Share your code") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Selected Pokémon", style = MaterialTheme.typography.titleMedium)
            Text("#$pokemonId — $pokemonName")

            Spacer(Modifier.height(12.dp))
            Text("Give this code to your partner:", style = MaterialTheme.typography.titleMedium)
            Text(
                text = code,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeShowCodeScreen(
    vm: TradeViewModel = viewModel(),
    pokemonId: Int,
    pokemonName: String,
    onBack: () -> Unit
) {
    // Creamos el Exchange
    val ui = vm.state.collectAsState()
    LaunchedEffect(pokemonId, pokemonName) {
        if (ui.value.exchangeId == null) {
            vm.createExchangeWithOfferA(pokemonId, pokemonName)
        }
    }

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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Selected Pokémon", style = MaterialTheme.typography.titleMedium)
            Text("#$pokemonId — $pokemonName")

            Spacer(Modifier.height(12.dp))
            when {
                ui.value.isBusy -> Text("Generating code…")
                ui.value.error != null -> Text(ui.value.error ?: "Error", color = MaterialTheme.colorScheme.error)
                else -> {
                    Text("Give this code to your partner:", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = ui.value.code ?: "------",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
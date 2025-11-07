package com.uvg.mypokedex.presentation.features.trade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvg.mypokedex.presentation.components.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeSelectScreen(
    viewModel: TradeSelectViewModel,
    onBack: () -> Unit,
    onPokemonSelected: (id: Int, name: String) -> Unit
) {
    // Tomamos favoritos del view model
    val favorites by viewModel.favorites.collectAsStateWithLifecycle(initialValue = emptyList())

    android.util.Log.d("TradeSelectUI", "render favorites size=${favorites.size}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Pokémon") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("You have no favorites yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites, key = { it.id }) { fav ->
                    PokemonCard(
                        favorite = fav,
                        onClick = { onPokemonSelected(fav.id, fav.name) }
                    )
                }
            }
        }
    }
}
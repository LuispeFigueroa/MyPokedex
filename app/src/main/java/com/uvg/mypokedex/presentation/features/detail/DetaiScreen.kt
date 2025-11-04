package com.uvg.mypokedex.presentation.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.uvg.mypokedex.domain2.model.Pokemon
import com.uvg.mypokedex.presentation.components.PokemonMeasurements
import com.uvg.mypokedex.presentation.components.PokemonSpecifics
import com.uvg.mypokedex.presentation.components.PokemonStatRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pokemon: Pokemon? = state.pokemon
    val currentError = state.error

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "Pokemon Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                actions = {
                    // Botón de favorito solo cuando hay data
                    if (pokemon != null) {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            if (state.isFavorite) {
                                Icon(Icons.Filled.Favorite, contentDescription = "Remove Favorite")
                            } else {
                                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Add as favorite")
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        // Si está cargando
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // Si ocurrió un error
        if (currentError != null && pokemon == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    currentError.message?.let { Text(it) }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.retry() }) { Text("Try again") }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = onBack) { Text("Go back") }
                }
            }
            return@Scaffold
        }

        // Detalle del pokemon (pokemon no nulo)
        pokemon ?: return@Scaffold

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Imagen y número
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = "Imagen de ${pokemon.name}",
                        modifier = Modifier.size(160.dp)
                    )
                    Text(
                        text = "#%03d".format(pokemon.id),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Medidas
            item {
                PokemonMeasurements(
                    height = pokemon.height,
                    weight = pokemon.weight
                )
            }

            // Tipos y habilidades
            item {
                PokemonSpecifics(
                    types = pokemon.types,
                    abilities = pokemon.abilities
                )
            }

            // Stats
            items(pokemon.stats.size) { idx ->
                val stat = pokemon.stats[idx]
                PokemonStatRow(stat = stat)
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

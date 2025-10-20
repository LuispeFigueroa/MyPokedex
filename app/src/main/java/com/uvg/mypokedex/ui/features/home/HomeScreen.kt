package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.mypokedex.data.repository.RepositoryProvider
import com.uvg.mypokedex.ui.components.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPokemonClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val repository = RepositoryProvider.pokemonRepository
    val vm: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            application = context.applicationContext as Application,
            repository = repository
        )
    )
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val list = vm.pokemonList

    var query by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            MediumTopAppBar(title = { Text("My Pokedex") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                value = query,
                onValueChange = {
                    query = it
                    vm.onSearchQueryChanged(it.text)
                },
                label = { Text("Buscar por nombre") },
                singleLine = true
            )

            // Empty
            if (!uiState.isLoading && uiState.errorMessage == null && list.isEmpty()) {
                EmptyState(onRetry = { vm.loadMorePokemon() })
                return@Column
            }

            // Error
            uiState.errorMessage?.let { msg ->
                ErrorState(
                    message = msg,
                    onRetry = { vm.loadMorePokemon() }
                )
            }

            // Lista
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                columns = GridCells.Adaptive(minSize = 160.dp)
            ) {
                val filtered = if (query.text.isBlank()) list
                else list.filter { it.name.contains(query.text, ignoreCase = true) }

                items(
                    items = filtered,
                    key = { it.id }
                ) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon.id) }
                    )
                }

                if (uiState.isLoading && filtered.isNotEmpty()) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            LaunchedEffect(list.size) {
                if (list.isEmpty() && !uiState.isLoading && uiState.errorMessage == null) {
                    vm.loadMorePokemon()
                }
            }
        }
    }
}


@Composable
private fun EmptyState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No hay pokémon aún.")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) { Text("Cargar") }
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(message)
            Spacer(Modifier.width(12.dp))
            OutlinedButton(onClick = onRetry) { Text("Reintentar") }
        }
    }
}


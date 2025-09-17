package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.OrderButton
import com.uvg.mypokedex.ui.components.PokemonCard

fun searchPokemon(searchText: String, pokemonList: List<Pokemon>): List<Pokemon> {
    return if (searchText.isBlank()) {
        pokemonList
    } else {
        pokemonList.filter { it.name.contains(searchText, ignoreCase = true) }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    favoriteIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onPokemonClick: (Int) -> Unit,
    onSearchClick: ()-> Unit
) {
    val pokemonList = viewModel.pokemonList
    var searchText by rememberSaveable { mutableStateOf("") }
    var isAscending by remember { mutableStateOf(true) } // Estado de orden de los pokemones

    LaunchedEffect(Unit) {
        viewModel.loadMorePokemon()
    }

    val gridState = rememberLazyGridState()
    LaunchedEffect(gridState, pokemonList) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                if (lastVisible != null && lastVisible >= pokemonList.size - 4) {
                    viewModel.loadMorePokemon()
                }
            }
    }

    val displayedPokemons by remember(searchText, isAscending) {
        derivedStateOf {
            val base = viewModel.pokemonList
            val filtered = searchPokemon(searchText, base)
            if (isAscending) {
                filtered.sortedBy { it.name.lowercase() }
            } else {
                filtered.sortedByDescending { it.name.lowercase() }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            OrderButton(
                isCurrentlyAscending = isAscending,
                onClick = { isAscending = !isAscending }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = searchText,
                label = {
                    Text("Filtrar por nombre")
                },
                onValueChange = { newText ->
                    searchText = newText
                },
                trailingIcon = {
                    IconButton(onClick = onSearchClick){
                        //el botoncito de ajustes permite abrir las herramientas

                        Icon(Icons.Filled.Settings, contentDescription = "Herramientas de busqeuda")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (displayedPokemons.isEmpty()) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "No se encontraron Pokemons"
                )
            } else {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = displayedPokemons,
                        key = { pokemon -> pokemon.id }
                    ) { p ->
                        PokemonCard(
                            pokemon = p,
                            onClick = { selectedPokemon ->
                                onPokemonClick(selectedPokemon.id)  // 👈 usa el callback
                            }
                        )
                    }
                }
            }
        }
    }
}

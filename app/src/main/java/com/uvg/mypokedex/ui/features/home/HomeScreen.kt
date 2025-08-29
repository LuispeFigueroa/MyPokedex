package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonCard
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

fun searchPokemon(searchText: String, pokemonList: List<Pokemon>): List<Pokemon> {
    if (searchText.isBlank()) {
        return pokemonList
    } else {
        return pokemonList.filter { it.name.contains(searchText, ignoreCase = true) }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = HomeViewModel(),
) {
    val pokemonList = viewModel.getPokemonList()
    var searchText by rememberSaveable { mutableStateOf("") }
    var filteredPokemons by remember { mutableStateOf(pokemonList) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(
            value = searchText,
            label = {
                Text("Filtrar por nombre")
            },
            onValueChange = {
                searchText = it
                filteredPokemons = searchPokemon(searchText, pokemonList)
            }
        )
        if (filteredPokemons.isEmpty()) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "No se encontraron Pokemons"
            )
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredPokemons) { p ->
            PokemonCard(
                pokemon = p,
            )
        }
    }
}
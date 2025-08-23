package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.ui.components.PokemonCard
import com.uvg.mypokedex.ui.components.PokemonMeasurements
import com.uvg.mypokedex.ui.components.UnstablePokemonList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = HomeViewModel()
){

    val pokemonList = viewModel.getPokemonList()
    val pokemonNames = pokemonList.map { it.name }

    Column {
        LazyVerticalGrid(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            columns = GridCells.Fixed(2)
        ) {
            items(pokemonList) { pokemon ->
                PokemonCard(pokemon = pokemon)
            }
        }

        UnstablePokemonList(pokemons = pokemonNames)
    }
}
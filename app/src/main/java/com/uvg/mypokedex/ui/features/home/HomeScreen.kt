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
import androidx.compose.material3.Scaffold
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
import com.uvg.mypokedex.ui.components.OrderButton



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
    favoriteIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
) {
    val pokemonList = viewModel.getPokemonList()
    var searchText by rememberSaveable { mutableStateOf("") }
    var isAscending by remember { mutableStateOf(true) } //estado de orden de los pokemones
    val displayedPokemons =remember(pokemonList, searchText, isAscending){
        searchPokemon(searchText, pokemonList)
            .let{ filteredList ->
                if (isAscending){
                    filteredList.sortedBy{it.name.lowercase() } //ordenar despues de que se haya buscado alguno por nombre
                }else{
                    filteredList.sortedByDescending { it.name.lowercase() } //ordenar de forma descendiente
                }
            }
    }

    Scaffold(
        floatingActionButton = {
            OrderButton(isCurrentlyAscending = isAscending,
                onClick = { isAscending = !isAscending}
            )
        }
    ){
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextField(
                value = searchText,
                label = {
                    Text("Filtrar por nombre")
                },
                onValueChange = {
                    newText ->
                    searchText = newText
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (displayedPokemons.isEmpty()) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "No se encontraron Pokemons"
                )
            }else{
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ){
                    items(
                        items = displayedPokemons,
                        key = {pokemon -> pokemon.id}
                    ){ p ->
                        PokemonCard(
                            pokemon = p,
                        )
                    }
                }
            }
        }
    }
}
package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats


class HomeViewModel {
    fun getPokemonList(): List<Pokemon> {
        return listOf(
            Pokemon(1, listOf("Hierba", "Veneno"),"Bulbasaur",50, Stats(70, 50, 85, 60), 125.5f, 100.0f),
            Pokemon(4, listOf("Fuego"), "Charmander", 6, Stats(75, 45, 99, 30), 100.0f, 185.5f),
            Pokemon(150, listOf("Psiquico"),"Mewtwo",200, Stats(72, 70, 78, 25),  120.0f, 190.5f),
        )
    }
}





package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats


class HomeViewModel {
    fun getPokemonList(): List<Pokemon> {
        return listOf(
            Pokemon(1, listOf("planta", "Veneno"),"Bulbasaur", Stats(50, 50,70, 50, 85, 60), 125.5f, 100.0f),
            Pokemon(4, listOf("fuego"), "Charmander",  Stats(60, 60,75, 45, 99, 30), 100.0f, 185.5f),
            Pokemon(150, listOf("psiquico"),"Mewtwo", Stats(200, 200,72, 70, 78, 25),  120.0f, 190.5f),
            Pokemon(7, listOf("agua"), "Squirtle", Stats(44, 48, 65, 50, 64, 43), 9.0f, 0.5f),
            Pokemon(25, listOf("eléctrico"), "Pikachu", Stats(35, 55, 40, 50, 50, 90), 6.0f, 0.4f),
            Pokemon(92, listOf("fantasma", "Veneno"), "Gastly", Stats(30, 35, 30, 100, 35, 80), 0.1f, 1.3f),
            Pokemon(133, listOf("normal"), "Eevee", Stats(55, 55, 50, 45, 65, 55), 6.5f, 0.3f),
            Pokemon(143, listOf("normal"), "Snorlax", Stats(160, 110, 65, 65, 110, 30), 460.0f, 2.1f),
            Pokemon(66, listOf("lucha"), "Machop", Stats(70, 80, 50, 35, 35, 35), 19.5f, 0.8f),
            Pokemon(77, listOf("fuego"), "Ponyta", Stats(50, 85, 55, 65, 65, 90), 30.0f, 1.0f)
        )
    }
}





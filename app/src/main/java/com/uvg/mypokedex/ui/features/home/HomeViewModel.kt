package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.Pokemon



class HomeViewModel {
    fun getPokemonList(): List<Pokemon> {
        return listOf(
            Pokemon(1, "Hierba / Veneno","Bulbasaur",50 ),
            Pokemon(4, "Fuego", "Charmander", 60),
            Pokemon(150, "Psiquico","Mewtwo",200),
        )
    }
}





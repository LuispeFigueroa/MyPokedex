package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.data.model.Pokemon

interface PokemonRepository {
    //llama a la api para obtener los pokemones
    suspend fun getPokemon(name: String): Result<Pokemon>
}
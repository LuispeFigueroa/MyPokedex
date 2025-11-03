package com.uvg.mypokedex.data2.pokemon.remote.api

import com.uvg.mypokedex.data2.pokemon.remote.dto.PokemonDto
import com.uvg.mypokedex.data2.pokemon.remote.dto.PokemonPageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    // Obtiene una página de pokemons de la API
    @GET("pokemon")
    suspend fun getPokemonPage(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonPageDto

    // Obtiene los detalles de un pokemon específico
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDto
}
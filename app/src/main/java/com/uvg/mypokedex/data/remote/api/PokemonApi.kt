package com.uvg.mypokedex.data.remote.api

import com.uvg.mypokedex.data.remote.dto.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi{
    @GET("pokemon/{name}")// path que se agrega al url
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDto
}
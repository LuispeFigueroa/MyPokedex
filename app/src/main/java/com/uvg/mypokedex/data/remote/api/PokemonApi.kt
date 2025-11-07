package com.uvg.mypokedex.data.remote.api

import com.uvg.mypokedex.data.remote.dto.PokemonDto
import com.uvg.mypokedex.data.remote.dto.PokemonPageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonPage(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonPageDto

    @GET("pokemon/{nameOrId}")
    suspend fun getPokemonDetail(
        @Path("nameOrId") nameOrId: String
    ): PokemonDto
}
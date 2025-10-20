package com.uvg.mypokedex.data.remote

import com.uvg.mypokedex.data.remote.api.PokemonApi
import com.uvg.mypokedex.data.remote.dto.PokemonDto
import com.uvg.mypokedex.data.remote.dto.PokemonPageDto

interface RemoteDataSource {
    suspend fun fetchPokemonPage(limit: Int, offset: Int): PokemonPageDto
    suspend fun fetchPokemonDetail(nameOrId: String): PokemonDto
}

class RemoteDataSourceImpl(
    private val api: PokemonApi
) : RemoteDataSource {

    override suspend fun fetchPokemonPage(limit: Int, offset: Int): PokemonPageDto {
        return api.getPokemonPage(limit = limit, offset = offset)
    }

    override suspend fun fetchPokemonDetail(nameOrId: String): PokemonDto {
        return api.getPokemonDetail(nameOrId)
    }
}

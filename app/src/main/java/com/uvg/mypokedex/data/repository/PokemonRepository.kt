package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.core.common.Result
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem
import com.uvg.mypokedex.domain.sort.PokemonOrder
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonDetail(nameOrId: String): Flow<Result<Pokemon>>

    val cachedPokemon: Flow<List<PokemonListItem>>

    suspend fun fetchAndCacheNextPage(currentCount: Int): Flow<Result<Unit>>

    val orderPreference: Flow<PokemonOrder>

    suspend fun changeOrderPreference(newOrder: PokemonOrder)

    val isConnected: Flow<Boolean>
}
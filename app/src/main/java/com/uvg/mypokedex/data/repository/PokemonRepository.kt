package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.core.common.Result
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun loadNextPage(currentCount: Int): Flow<com.uvg.mypokedex.core.common.Result<List<PokemonListItem>>>

    fun getPokemonDetail(nameOrId: String): Flow<Result<Pokemon>>
}
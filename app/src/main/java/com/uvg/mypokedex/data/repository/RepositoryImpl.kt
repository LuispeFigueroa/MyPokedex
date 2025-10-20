package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.core.common.Result
import com.uvg.mypokedex.core.network.ErrorMapper
import com.uvg.mypokedex.data.remote.RemoteDataSource
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem
import com.uvg.mypokedex.data.remote.mapper.toDomain
import com.uvg.mypokedex.data.remote.mapper.toPokemon
import com.uvg.mypokedex.data.remote.paging.PokemonPaging
import com.uvg.mypokedex.data.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers

class PokemonRepositoryImpl(
    private val remote: RemoteDataSource
) : PokemonRepository {

    override fun loadNextPage(currentCount: Int): Flow<Result<List<PokemonListItem>>> = flow {
        emit(Result.Loading)
        try {
            val limit = PokemonPaging.PAGE_SIZE
            val offset = PokemonPaging.nextOffset(currentCount)
            val page = remote.fetchPokemonPage(limit, offset)
            val items = page.results.map { it.toDomain() }
            emit(Result.Success(items))
        } catch (t: Throwable) {
            val ui = ErrorMapper.toUiError(t)
            emit(Result.Error(ui.message, t))
        }
    }.flowOn(Dispatchers.IO)

    override fun getPokemonDetail(nameOrId: String): Flow<Result<Pokemon>> = flow {
        emit(Result.Loading)
        try {
            val dto = remote.fetchPokemonDetail(nameOrId)
            val pokemon = dto.toPokemon()
            emit(Result.Success(pokemon))
        } catch (t: Throwable) {
            val ui = ErrorMapper.toUiError(t)
            emit(Result.Error(ui.message, t))
        }
    }.flowOn(Dispatchers.IO)
}

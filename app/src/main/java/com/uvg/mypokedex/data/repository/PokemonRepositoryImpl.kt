package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.core.common.Result
import com.uvg.mypokedex.core.network.NetworkMonitor
import com.uvg.mypokedex.data.local.db.CachedPokemon
import com.uvg.mypokedex.data.local.db.PokemonDao
import com.uvg.mypokedex.data.local.db.toCachedPokemon
import com.uvg.mypokedex.data.local.preferences.UserPreferencesDataStore
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.remote.RemoteDataSource
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem
import com.uvg.mypokedex.data.remote.mapper.toPokemon
import com.uvg.mypokedex.data.remote.paging.PokemonPaging
import com.uvg.mypokedex.domain.sort.PokemonOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val prefs: UserPreferencesDataStore,
    private val remote: RemoteDataSource,
    private val networkMonitor: NetworkMonitor,
    private val pokemonDao: PokemonDao
) : PokemonRepository {
    override fun getPokemonDetail(nameOrId: String): Flow<Result<Pokemon>> = flow {
        emit(Result.Loading)
        try {
            val dto = remote.fetchPokemonDetail(nameOrId)
            val pokemon = dto.toPokemon()
            emit(Result.Success(pokemon))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error obteniendo detalle de Pokémon", e))
        }
    }.flowOn(Dispatchers.IO)

    override val cachedPokemon: Flow<List<PokemonListItem>> =
        pokemonDao.getAllPokemon()
            .map { cachedList ->
                cachedList.map { cached ->
                    PokemonListItem(
                        id = cached.id,
                        name = cached.name
                    )
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun fetchAndCacheNextPage(currentCount: Int): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        val connected = isConnected.firstOrNull() ?: true
        if (!connected) {
            emit(Result.Error("Sin conexión"))
            return@flow
        }

        try {
            val limit = PokemonPaging.PAGE_SIZE
            val offset = PokemonPaging.nextOffset(currentCount)

            val page = remote.fetchPokemonPage(limit, offset)

            val now = System.currentTimeMillis()
            val entities: List<CachedPokemon> = page.results.map { dto ->
                dto.toCachedPokemon(now)
            }

            pokemonDao.upsertAll(entities)
            emit(Result.Success(Unit))
        } catch (t: Throwable) {
            emit(Result.Error("Error actualizando caché", t))
        }
    }.flowOn(Dispatchers.IO)

    override val orderPreference: Flow<PokemonOrder> = prefs.orderFlow

    override suspend fun changeOrderPreference(newOrder: PokemonOrder) {
        prefs.setOrder(newOrder)
    }

    override val isConnected: Flow<Boolean> = networkMonitor.isConnected
}


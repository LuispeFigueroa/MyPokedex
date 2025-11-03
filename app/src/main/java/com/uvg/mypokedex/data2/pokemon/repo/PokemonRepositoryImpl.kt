package com.uvg.mypokedex.data2.pokemon.repo

import com.uvg.mypokedex.core2.network.safeApiCall
import com.uvg.mypokedex.data2.pokemon.constants.PokemonConstants.DETAIL_CACHE_CAP
import com.uvg.mypokedex.data2.pokemon.constants.PokemonConstants.PAGE_SIZE
import com.uvg.mypokedex.data2.pokemon.local.PokemonLocalDataSource
import com.uvg.mypokedex.data2.pokemon.mapper.toDetailEntity
import com.uvg.mypokedex.data2.pokemon.mapper.toNamedItem
import com.uvg.mypokedex.data2.pokemon.mapper.toPokemon
import com.uvg.mypokedex.data2.pokemon.mapper.toSummaryEntity
import com.uvg.mypokedex.data2.pokemon.mapper.toSummaryEntityOrNull
import com.uvg.mypokedex.data2.pokemon.prefs.PokemonSortOrderDataSource
import com.uvg.mypokedex.data2.pokemon.prefs.SortOrder
import com.uvg.mypokedex.data2.pokemon.remote.PokemonRemoteDataSource
import com.uvg.mypokedex.domain2.common.AppError
import com.uvg.mypokedex.domain2.model.NamedItem
import com.uvg.mypokedex.domain2.model.Pokemon
import com.uvg.mypokedex.domain2.repo.PokemonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PokemonRepositoryImpl(
    private val remote: PokemonRemoteDataSource,
    private val local: PokemonLocalDataSource,
    private val prefs: PokemonSortOrderDataSource
): PokemonRepository {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
    private val writeMutex = Mutex()

    /*
     PREFERENCIAS (DataStore)
     */

    // Contiene el orden actual de la lista de pokemons
    override val orderPreference: Flow<SortOrder> = prefs.sortOrderFlow

    // Cambia el orden de la lista de pokemons
    override suspend fun changeOrderPreference(newOrder: SortOrder) {
        prefs.setSortOrder(newOrder)
    }

    /*
     LISTA DESDE INTERNET (desde API a cache local)
     */

    // Obtiene un número determinado de pokemons de la API y los almacena en caché
    override suspend fun fetchAndCacheNextPage(): Result<Unit> {
        val offset = local.countSummaries()
        val limit = PAGE_SIZE

        // Se hace una llamada segura a la API
        val page = safeApiCall {
            remote.fetchPage(limit = limit, offset = offset)
        }

        return page.mapCatching { dto ->
            if (dto.results.isEmpty()) {
                throw AppError.EndOfPagination
            }

            // Se crean las entradas en la tabla de summaries de la base de datos
            val now = System.currentTimeMillis()
            val summaries = dto.results.mapNotNull { it.toSummaryEntityOrNull(now) }

            // Se escriben las entradas en la base de datos
            writeMutex.withLock {
                local.upsertSummaries(summaries)
            }
        }
    }

    /*
     DETALLE DESDE INTERNET (desde API a cache local)
     */

    // Obtiene el detalle de un pokemon y lo almacena en caché
    override suspend fun fetchAndCachePokemonDetail(id: Int): Result<Unit> {
        val now = System.currentTimeMillis()

        val detail = safeApiCall { remote.fetchDetail(id) }

        return detail.mapCatching { dto ->
            val detailEntity = dto.toDetailEntity(now, json)
            val summaryEntity = dto.toSummaryEntity(now)

            writeMutex.withLock {
                local.upsertSummaries(listOf(summaryEntity))
                local.upsertDetail(detailEntity)

                val total = local.countDetails()
                if (total > DETAIL_CACHE_CAP) {
                    local.evictOldestDetails(keep = DETAIL_CACHE_CAP)
                }
            }
        }
    }

    /*
     LISTA DESDE CACHÉ (Room)
     */

    // Obtiene los pokemon summaries almacenados en la caché
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCachedPokemon(): Flow<List<NamedItem>> {
        return prefs.sortOrderFlow
            .distinctUntilChanged()
            .flatMapLatest { order ->
                local.getSummaries(order  = order)
            }
            .map { entities -> entities.map { it.toNamedItem() } }
    }

    /*
     DETALLES DESDE CACHÉ (Room)
     */

    // Obtiene los detalles de pokemons almacenados en la caché
    override fun observeCachedDetail(id: Int): Flow<Pokemon?> {
        return local.getDetail(id).map { it?.toPokemon(json) }
    }
}
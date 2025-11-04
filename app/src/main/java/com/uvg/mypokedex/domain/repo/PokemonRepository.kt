package com.uvg.mypokedex.domain.repo

import com.uvg.mypokedex.data.pokemon.prefs.SortOrder
import com.uvg.mypokedex.domain.model.NamedItem
import com.uvg.mypokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    // Contiene el orden actual de la lista de pokemons
    val orderPreference: Flow<SortOrder>

    // Cambia el orden de la lista de pokemons
    suspend fun changeOrderPreference(newOrder: SortOrder)

    // Obtiene un número determinado de pokemons de la API y los almacena en caché
    suspend fun fetchAndCacheNextPage(): Result<Unit>

    // Obtiene los detalles de un pokemon específico y lo almacena en caché
    suspend fun fetchAndCachePokemonDetail(id: Int): Result<Unit>

    // Obtiene los pokemons almacenados en la caché
    fun observeCachedPokemon(): Flow<List<NamedItem>>

    // Obtiene el detalle de un pokemon almacenado en caché
    fun observeCachedDetail(id: Int): Flow<Pokemon?>
}
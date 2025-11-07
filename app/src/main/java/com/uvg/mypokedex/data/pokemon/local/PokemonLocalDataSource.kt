package com.uvg.mypokedex.data.pokemon.local

import com.uvg.mypokedex.data.pokemon.local.dao.PokemonDao
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonDetailEntity
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonSummaryEntity
import com.uvg.mypokedex.data.pokemon.prefs.SortOrder
import kotlinx.coroutines.flow.Flow

// Clase para manejar la fuente de datos locales (provenientes de la base de datos de Room)
class PokemonLocalDataSource(private val dao: PokemonDao) {
    // Devuelve una página de pokemones desde cache local, ordenada según SortOrder
    fun getSummaries(order: SortOrder): Flow<List<CachedPokemonSummaryEntity>> =
        dao.getSummaries(order.name)

    // Inserta/actualiza una lista de summaries de pokemones (solo nombre e imágen)
    suspend fun upsertSummaries(items: List<CachedPokemonSummaryEntity>) =
        dao.upsertSummaries(items)

    //  Lee un detalle desde cache (o null si no existe)
    fun getDetail(id: Int): Flow<CachedPokemonDetailEntity?> = dao.getDetail(id)

    // Inserta/actualiza el detalle de pokemon
    suspend fun upsertDetail(item: CachedPokemonDetailEntity) =
        dao.upsertDetail(item)

    // Cantidad total de summaries en caché
    suspend fun countSummaries(): Int = dao.countSummaries()

    // Cantidad total de details en caché
    suspend fun countDetails(): Int = dao.countDetails()

    // Elimina los pokemon details más viejos cuando se llegó al tope de caché
    suspend fun evictOldestDetails(keep: Int) =
        dao.evictOldestDetails(keep)
}
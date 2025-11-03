package com.uvg.mypokedex.data2.pokemon.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uvg.mypokedex.data2.pokemon.local.entity.CachedPokemonDetailEntity
import com.uvg.mypokedex.data2.pokemon.local.entity.CachedPokemonSummaryEntity
import kotlinx.coroutines.flow.Flow

// DAO para interactuar con la base de datos
@Dao
interface PokemonDao {
    // Consulta para obtener una página de pokemons desde la base de datos local
    @Query("""
    SELECT * FROM cached_pokemon_summary
    ORDER BY 
      CASE WHEN :order = 'ID_ASC'    THEN id   END ASC,
      CASE WHEN :order = 'ID_DESC'   THEN id   END DESC,
      CASE WHEN :order = 'NAME_ASC'  THEN name END ASC,
      CASE WHEN :order = 'NAME_DESC' THEN name END DESC
    """)
    fun getSummaries(order: String): Flow<List<CachedPokemonSummaryEntity>>

    // Obtiene más pokemones de la API y los guarda en la base de datos local
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSummaries(items: List<CachedPokemonSummaryEntity>)

    // Consulta para obtener los detalles de un pokemon específico
    @Query("SELECT * FROM cached_pokemon_detail WHERE id = :id")
    fun getDetail(id: Int): Flow<CachedPokemonDetailEntity?>

    // Inserta o actualiza los detalles de un pokemon en la base de datos local
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDetail(item: CachedPokemonDetailEntity)

    // Cuenta la cantidad de pokemon summaries en la base de datos local
    @Query("SELECT COUNT(*) FROM cached_pokemon_summary")
    suspend fun countSummaries(): Int

    // Cuenta la cantidad de pokemon details en la base de datos local
    @Query("SELECT COUNT(*) FROM cached_pokemon_detail")
    suspend fun countDetails(): Int

    // Elimina los pokemon details más viejos cuando se llegó al tope de caché
    @Query("""
    DELETE FROM cached_pokemon_detail
    WHERE id NOT IN (
        SELECT id FROM cached_pokemon_detail
        ORDER BY lastFetchedAt DESC
        LIMIT :keep
    )
""")
    suspend fun evictOldestDetails(keep: Int)
}

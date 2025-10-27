package com.uvg.mypokedex.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM cached_pokemon ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<CachedPokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(pokemon: List<CachedPokemon>)
}

package com.uvg.mypokedex.data.pokemon.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uvg.mypokedex.data.pokemon.local.dao.PokemonDao
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonDetailEntity
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonSummaryEntity

// Base de datos Room para la aplicación
@Database(
    entities = [CachedPokemonSummaryEntity::class, CachedPokemonDetailEntity::class],
    version = 9, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
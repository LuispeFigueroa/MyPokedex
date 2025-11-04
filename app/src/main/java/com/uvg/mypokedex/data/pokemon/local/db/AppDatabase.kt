package com.uvg.mypokedex.data.pokemon.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uvg.mypokedex.data.pokemon.local.converter.RoomConverters
import com.uvg.mypokedex.data.pokemon.local.dao.PokemonDao
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonDetailEntity
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonSummaryEntity

// Base de datos Room para la aplicación
@Database(
    entities = [CachedPokemonSummaryEntity::class, CachedPokemonDetailEntity::class],
    version = 6, exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
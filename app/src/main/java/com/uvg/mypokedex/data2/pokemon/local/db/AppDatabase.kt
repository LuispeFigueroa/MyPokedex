package com.uvg.mypokedex.data2.pokemon.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uvg.mypokedex.data2.pokemon.local.converter.RoomConverters
import com.uvg.mypokedex.data2.pokemon.local.dao.PokemonDao
import com.uvg.mypokedex.data2.pokemon.local.entity.CachedPokemonDetailEntity
import com.uvg.mypokedex.data2.pokemon.local.entity.CachedPokemonSummaryEntity

// Base de datos Room para la aplicación
@Database(
    entities = [CachedPokemonSummaryEntity::class, CachedPokemonDetailEntity::class],
    version = 1, exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
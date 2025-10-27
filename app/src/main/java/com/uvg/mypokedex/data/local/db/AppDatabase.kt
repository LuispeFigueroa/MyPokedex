package com.uvg.mypokedex.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CachedPokemon::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
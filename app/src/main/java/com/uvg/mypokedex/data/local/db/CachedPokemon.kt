package com.uvg.mypokedex.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_pokemon")
data class CachedPokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: String,
    val statsJson: String,
    val lastFetchedAt: Long
)
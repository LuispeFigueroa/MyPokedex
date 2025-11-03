package com.uvg.mypokedex.data2.pokemon.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla de Room que guarda el resumen de información de un pokemon
@Entity(tableName = "cached_pokemon_summary")
data class CachedPokemonSummaryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val lastFetchedAt: Long
)
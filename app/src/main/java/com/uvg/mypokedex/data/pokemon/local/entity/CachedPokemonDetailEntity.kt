package com.uvg.mypokedex.data.pokemon.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla de Room que guarda los detalles de un pokemon
@Entity(tableName = "cached_pokemon_detail")
data class CachedPokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Float,
    val weight: Float,
    val typesJson: String,
    val abilitiesJson: String,
    val statsJson: String,
    val imageUrl: String,
    val lastFetchedAt: Long
)
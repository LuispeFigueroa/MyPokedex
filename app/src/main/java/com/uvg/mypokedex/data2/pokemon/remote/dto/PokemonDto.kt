package com.uvg.mypokedex.data2.pokemon.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO de la respuesta de la API al pedir un pokemon específico
@Serializable
data class PokemonDto(
    val id: Int,                                            // Número del pokemon en la pokedex
    val name: String,                                       // Nombre del pokemon
    val height: Float,                                      // Altura del pokemon en decímetros
    val weight: Float,                                      // Peso del pokemon en hectogramos
    val types: List<PokemonTypeDto> = emptyList(),          // Tipos del pokemon (subDTO)
    val abilities: List<PokemonAbilityDto> = emptyList(),   // Habilidades del pokemon (subDTO)
    val stats: List<PokemonStatDto> = emptyList()           // Stats del pokemon (subDTO)
)

// DTO de los tipos de un pokemon
@Serializable
data class PokemonTypeDto(
    val slot: Int,          // Prioridad del tipo (slot 1 es el tipo primario)
    val type: NamedItemDto  // Tipo (tiene nombre y URL)
)

// DTO de las habilidades de un pokemon
@Serializable
data class PokemonAbilityDto(
    @SerialName("is_hidden") val isHidden: Boolean, // Si la habilidad es oculta
    val ability: NamedItemDto,                             // Habilidad (tiene nombre y URL)
)

// DTO de las stats de un pokemon
@Serializable
data class PokemonStatDto(
    @SerialName("base_stat") val baseStat: Int,  // Valor de la stat
    val stat: NamedItemDto                              // Stat (tiene nombre y URL)
)

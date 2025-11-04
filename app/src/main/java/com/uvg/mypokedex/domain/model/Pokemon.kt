package com.uvg.mypokedex.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Clase modelo de la información de un pokemon
data class Pokemon(
    val id: Int,                            // Número del pokemon en la pokedex
    val name: String,                       // Nombre del pokemon
    val height: Float,                      // Altura del pokemon en decímetros
    val weight: Float,                      // Peso del pokemon en hectogramos
    val types: List<PokemonType>,           // Tipos del pokemon (subclase)
    val abilities: List<PokemonAbility>,    // Habilidades del pokemon (subclase)
    val stats: List<PokemonStat>,           // Stats del pokemon (subclase)
    val imageUrl: String                    // URL de la imágen del pokemon
)

/*
Subclases (Serializables para poder ser convertidas a JSON en RoomConverters,kt y así poder guardar
sus datos en la base de datos de Room)
 */

// Subclase para un tipo de un pokemon
@Serializable
data class PokemonType(
    val slot: Int,       // Prioridad del tipo (slot 1 es el tipo primario)
    val type: NamedItem  // Tipo (tiene nombre y URL)
)

// Subclase para una habilidad de un pokemon
@Serializable
data class PokemonAbility(
    @SerialName("is_hidden")
    val isHidden: Boolean,   // Si la habilidad es oculta
    val ability: NamedItem,  // Habilidad (tiene nombre y URL)
)

// Subclase para una stat de un pokemon
@Serializable
data class PokemonStat(
    @SerialName("base_stat")
    val baseStat: Int,    // Valor de la stat
    val stat: NamedItem   // Stat (tiene nombre y URL)
)

// Clase con información de un elemento específico (Serializable para poder ser convertida a JSON)
@Serializable
data class NamedItem(
    val name: String,   // Nombre del elemento
    val url: String     // URL del elemento
)
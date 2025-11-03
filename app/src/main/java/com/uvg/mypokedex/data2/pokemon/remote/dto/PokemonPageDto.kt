package com.uvg.mypokedex.data2.pokemon.remote.dto

import kotlinx.serialization.Serializable

// DTO de la respuesta de la API al pedir una página
@Serializable
data class PokemonPageDto(
    val count: Int,                 // Número total de pokemons en la API
    val next: String? = null,       // URL a la página siguiente
    val previous: String? = null,   // URL a la página anterior
    val results: List<NamedItemDto>    // Lista de pokemons en la página (cada uno tiene nombre y URL)
)

// DTO pequeño de la información que la API devuelve de un elemento
@Serializable
data class NamedItemDto(
    val name: String,   // Nombre del elemento
    val url: String     // URL del elemento
)
package com.uvg.mypokedex.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonPageDto(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonListItemDto>
)

@Serializable
data class PokemonListItemDto(
    val name: String,
    val url: String
)

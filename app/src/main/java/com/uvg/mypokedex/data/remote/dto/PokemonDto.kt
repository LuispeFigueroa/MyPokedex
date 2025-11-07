package com.uvg.mypokedex.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val types: List<TypeDto>,
    val weight: Int,
    val height: Int,
    val stats: List<StatDto>
)

@Serializable
data class TypeDto(val type: TypeNameDto)

@Serializable
data class TypeNameDto(val name: String)

@Serializable
data class StatDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: StatNameDto
)

@Serializable
data class StatNameDto(val name: String)
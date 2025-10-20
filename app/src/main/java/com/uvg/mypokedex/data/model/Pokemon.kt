package com.uvg.mypokedex.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    val type: List<String>,
    val weight: Float,
    val height: Float,
    val stats: Stats,
)

@Serializable
    data class Stats(
    val hp: Int,
    val hpCurrent: Int = hp,
    val attack: Int,
    val defense: Int,
    @SerialName("special_attack") val specialAttack: Int,
    @SerialName("special_defense") val specialDefense: Int,
    val speed: Int
)
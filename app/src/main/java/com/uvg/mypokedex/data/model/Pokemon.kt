package com.uvg.mypokedex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val type: List<String>,
    val name: String,
    val stats: Stats,
    val weight: Float,
    val height: Float
)
@Serializable
data class Stats(
    val health: Int,
    val maxHealth: Int,
    val Attack: Int,
    val Defense: Int,
    val SpecialAttack: Int,
    val Speed: Int
)
package com.uvg.mypokedex.data.model

data class Pokemon(
    val id: Int,
    val types: List<String>,
    val name: String,
    val health: Int,
    val maxHealth: Int,
    val stats: Stats,
    val weight: Float,
    val height: Float
)

data class Stats(
    val Attack: Int,
    val Defense: Int,
    val SpecialAttack: Int,
    val Speed: Int
)
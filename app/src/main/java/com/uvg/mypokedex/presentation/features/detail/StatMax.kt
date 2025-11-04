package com.uvg.mypokedex.presentation.features.detail

// Máximo para cada stat segun la pokeapi (útil para las barras de progreso)
object StatMax {
    const val HP = 255            // Blissey
    const val ATTACK = 190        // Mega Mewtwo X
    const val DEFENSE = 250       // Eternatus-Eternamax
    const val SP_ATTACK = 194     // Mega Mewtwo Y
    const val SP_DEFENSE = 250    // Eternatus-Eternamax
    const val SPEED = 200         // Regieleki

    fun maxFor(statName: String): Int = when (statName.lowercase()) {
        "hp" -> HP
        "attack" -> ATTACK
        "defense" -> DEFENSE
        "special-attack" -> SP_ATTACK
        "special-defense" -> SP_DEFENSE
        "speed" -> SPEED
        else -> 255 // fallback razonable
    }
}
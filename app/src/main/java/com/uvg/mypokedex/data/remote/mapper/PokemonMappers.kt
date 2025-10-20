package com.uvg.mypokedex.data.remote.mapper

import com.uvg.mypokedex.data.remote.dto.PokemonDto
import com.uvg.mypokedex.data.remote.dto.PokemonListItemDto
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats

fun PokemonListItemDto.idFromUrl(): Int =
    url.trimEnd('/').substringAfterLast('/').toInt()

data class PokemonListItem(
    val id: Int,
    val name: String
)

fun PokemonListItemDto.toDomain(): PokemonListItem =
    PokemonListItem(id = idFromUrl(), name = name)

fun PokemonDto.toPokemon(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        type = types.map { it.type.name },
        weight = weight.toFloat(),
        height = height.toFloat(),
        stats = Stats(
            hp = stats.find { it.stat.name == "hp" }?.baseStat ?: 0,
            attack = stats.find { it.stat.name == "attack" }?.baseStat ?: 0,
            defense = stats.find { it.stat.name == "defense" }?.baseStat ?: 0,
            specialAttack = stats.find { it.stat.name == "special-attack" }?.baseStat ?: 0,
            specialDefense = stats.find { it.stat.name == "special-defense" }?.baseStat ?: 0,
            speed = stats.find { it.stat.name == "speed" }?.baseStat ?: 0,
        )
    )
}

package com.uvg.mypokedex.data.local.db

import com.uvg.mypokedex.data.remote.dto.PokemonListItemDto
import com.uvg.mypokedex.data.remote.mapper.idFromUrl

fun PokemonListItemDto.toCachedPokemon(now: Long): CachedPokemon {
    return CachedPokemon(
        id = idFromUrl(),
        name = name,
        imageUrl = "",
        types = "",
        statsJson = "",
        lastFetchedAt = now
    )
}
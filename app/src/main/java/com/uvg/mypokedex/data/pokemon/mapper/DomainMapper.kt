package com.uvg.mypokedex.data.pokemon.mapper

import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonDetailEntity
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonSummaryEntity
import com.uvg.mypokedex.domain.model.NamedItem
import com.uvg.mypokedex.domain.model.Pokemon
import kotlinx.serialization.json.Json

fun CachedPokemonSummaryEntity.toNamedItem(): NamedItem =
    NamedItem(
        name = name,
        url = "https://pokeapi.co/api/v2/pokemon/$id/"
    )

fun CachedPokemonDetailEntity.toPokemon(
    json: Json = Json { ignoreUnknownKeys = true; explicitNulls = false }
): Pokemon =
    Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        types = json.decodeFromString(typesJson),
        abilities = json.decodeFromString(abilitiesJson),
        stats = json.decodeFromString(statsJson),
        imageUrl = spriteUrl(id)
    )
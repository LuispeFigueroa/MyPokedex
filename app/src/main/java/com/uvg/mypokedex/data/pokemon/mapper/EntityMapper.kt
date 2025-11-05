package com.uvg.mypokedex.data.pokemon.mapper

import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonDetailEntity
import com.uvg.mypokedex.data.pokemon.local.entity.CachedPokemonSummaryEntity
import com.uvg.mypokedex.data.pokemon.remote.dto.NamedItemDto
import com.uvg.mypokedex.data.pokemon.remote.dto.PokemonDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/*
 MAPPERS
*/

// Mapea el DTO de un NamedItem a un entity de la base de datos
fun NamedItemDto.toSummaryEntityOrNull(now: Long): CachedPokemonSummaryEntity? {
    val id = extractIdOrNull() ?: return null
    return CachedPokemonSummaryEntity(
        id = id,
        name = name,
        imageUrl = spriteUrl(id),
        lastFetchedAt = now
    )
}

// Mapea el DTO de un pokemon a un entity de la base de datos
fun PokemonDto.toSummaryEntity(now: Long): CachedPokemonSummaryEntity =
    CachedPokemonSummaryEntity(
        id = id,
        name = name,
        imageUrl = spriteUrl(id),
        lastFetchedAt = now
    )

// Mapea el DTO de un pokemon con detalle a un entity de la base de datos
fun PokemonDto.toDetailEntity(
    now: Long,
    json: Json = Json { ignoreUnknownKeys = true; explicitNulls = false }
): CachedPokemonDetailEntity =
    CachedPokemonDetailEntity(
        id = id,
        name = name,
        height = height / 10,   // Se pasa a metros
        weight = weight / 10,   // Se pasa a kilogramos
        typesJson = json.encodeToString(types),
        abilitiesJson = json.encodeToString(abilities),
        statsJson = json.encodeToString(stats),
        imageUrl = spriteUrl(id),
        lastFetchedAt = now
    )


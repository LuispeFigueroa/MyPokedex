package com.uvg.mypokedex.data2.pokemon.mapper

import com.uvg.mypokedex.data2.pokemon.remote.dto.NamedItemDto
import com.uvg.mypokedex.domain2.model.NamedItem

// URL base para las imágenes de los pokemon
private const val BASE_SPRITE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"

// Función que agrega el id del pokemon a la URL base para obtener su imágen específica
fun spriteUrl(id: Int) = "$BASE_SPRITE_URL/$id.png"

// Función que extrae el ID de una URL en un NamedItemDto
fun NamedItemDto.extractIdOrNull(): Int? =
    url.trimEnd('/').substringAfterLast('/').toIntOrNull()

// Función que extrae el id de la URL de un NamedItem
fun NamedItem.idFromUrl(): Int? =
    url.trimEnd('/').substringAfterLast('/').toIntOrNull()
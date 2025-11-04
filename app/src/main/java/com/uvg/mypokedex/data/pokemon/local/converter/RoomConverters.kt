package com.uvg.mypokedex.data.pokemon.local.converter

import androidx.room.TypeConverter
import com.uvg.mypokedex.domain.model.PokemonAbility
import com.uvg.mypokedex.domain.model.PokemonStat
import com.uvg.mypokedex.domain.model.PokemonType
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

// Conversores de datos complejos a datos que pueden ser guardados por Room
class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    @TypeConverter
    fun typesToJson(value: List<PokemonType>): String =
        json.encodeToString(ListSerializer(PokemonType.serializer()), value)

    @TypeConverter
    fun jsonToTypes(value: String): List<PokemonType> =
        json.decodeFromString(ListSerializer(PokemonType.serializer()), value)

    @TypeConverter
    fun abilitiesToJson(value: List<PokemonAbility>): String =
        json.encodeToString(ListSerializer(PokemonAbility.serializer()), value)

    @TypeConverter
    fun jsonToAbilities(value: String): List<PokemonAbility> =
        json.decodeFromString(ListSerializer(PokemonAbility.serializer()), value)

    @TypeConverter
    fun statsToJson(value: List<PokemonStat>): String =
        json.encodeToString(ListSerializer(PokemonStat.serializer()), value)

    @TypeConverter
    fun jsonToStats(value: String): List<PokemonStat> =
        json.decodeFromString(ListSerializer(PokemonStat.serializer()), value)
}

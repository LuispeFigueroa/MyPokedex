package com.uvg.mypokedex.data.remote.dto

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    // se apega al JSon que regresa el APi de pokemon
    val id: Int,
    val name: String,
    val types: List<TypeDto>, // se va a manejar como una lista de objetos
    val weight: Float,
    val height: Float,
    val stats: List<StatDto> // igual que types, se tien que manjear como una lista de objetos
)
@Serializable
data class TypeDto(
    val type: TypeNameDto
)
@Serializable
data class TypeNameDto(
    val name: String
)
@Serializable
data class StatDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: StatNameDto
)
@Serializable
data class StatNameDto(
    val name: String
)

//funcion para convertir al pokemon
fun PokemonDto.toPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        type = this.types.map { it.type.name },
        weight = this.weight,
        height = this.height,
        stats = Stats(
            //busca cada stat, con un valor predeterminado si no encuentra uno, para evitar errores
            hp = this.stats.find { it.stat.name == "hp" }?.baseStat ?: 0,
            attack = this.stats.find { it.stat.name == "attack" }?.baseStat ?: 0,
            defense = this.stats.find { it.stat.name == "defense" }?.baseStat ?: 0,
            specialAttack = this.stats.find { it.stat.name == "special-attack" }?.baseStat ?: 0,
            specialDefense = this.stats.find { it.stat.name == "special-defense" }?.baseStat ?: 0,
            speed = this.stats.find { it.stat.name == "speed" }?.baseStat ?: 0,
        )
    )
}
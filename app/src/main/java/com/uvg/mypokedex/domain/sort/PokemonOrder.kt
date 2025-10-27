package com.uvg.mypokedex.domain.sort

enum class PokemonOrder(val rawValue: String) {
    NUMBER_ASC("number_asc"),
    NUMBER_DESC("number_desc"),
    NAME_ASC("name_asc"),
    NAME_DESC("name_desc");

    companion object {
        fun fromRaw(raw: String): PokemonOrder {
            return entries.find { it.rawValue == raw } ?: NUMBER_ASC
        }
    }
}
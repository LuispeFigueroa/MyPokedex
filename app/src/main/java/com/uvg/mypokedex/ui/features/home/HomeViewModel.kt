package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val pokemonList = mutableStateListOf<Pokemon>()
    private var currentPage = 0
    private var isLoading = false
    private var hasMore = true

    fun getFileNameForCurrentPage(currentPage: Int): String {
        val startIndex = currentPage * 10 + 1
        val endIndex = startIndex + 9

        return "pokemon_${startIndex.toString().padStart(3, '0')}_${endIndex.toString().padStart(3, '0')}.json"
    }

    fun loadMorePokemon() {
        if (isLoading || !hasMore) return
        isLoading = true

        viewModelScope.launch {
            val fileName = getFileNameForCurrentPage(currentPage)
            val result = runCatching {
                val text = withContext(Dispatchers.IO) {
                    getApplication<Application>()
                        .assets
                        .open(fileName)
                        .bufferedReader()
                        .use { it.readText() }
                }
                val root = Json.parseToJsonElement(text).jsonObject
                val items = root["items"]?.jsonArray ?: JsonArray(emptyList())

                items.map { el ->
                    val obj = el.jsonObject

                    val types = obj["type"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()

                    val statsMap: Map<String, Int> =
                        obj["stats"]?.jsonArray?.associate { stEl ->
                            val sObj = stEl.jsonObject
                            val name = sObj["name"]!!.jsonPrimitive.content
                            val value = sObj["value"]!!.jsonPrimitive.int
                            name to value
                        } ?: emptyMap()

                    Pokemon(
                        id = obj["id"]!!.jsonPrimitive.int,
                        name = obj["name"]!!.jsonPrimitive.content,
                        type = types,
                        weight = obj["weight"]!!.jsonPrimitive.double.toFloat(),
                        height = obj["height"]!!.jsonPrimitive.double.toFloat(),
                        stats = Stats(
                            hp = statsMap["hp"] ?: 0,
                            hpCurrent = statsMap["hp"] ?: 0,
                            attack = statsMap["attack"] ?: 0,
                            defense = statsMap["defense"] ?: 0,
                            specialAttack = statsMap["special-attack"] ?: 0,
                            specialDefense = statsMap["special-defense"] ?: 0,
                            speed = statsMap["speed"] ?: 0
                        )
                    )
                }
            }

            result.onSuccess { newItems ->
                if (newItems.isEmpty()) {
                    hasMore = false
                } else {
                    pokemonList.addAll(newItems)
                    currentPage += 1
                }
            }
            isLoading = false
        }
    }
}





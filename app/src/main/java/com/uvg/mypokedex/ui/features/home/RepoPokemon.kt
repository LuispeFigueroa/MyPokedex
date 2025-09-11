package com.uvg.mypokedex.ui.features.home


import android.content.Context
import com.uvg.mypokedex.data.model.Pokemon
import kotlinx.serialization.json.Json


class RepoPokemon(
    private val context: Context,
    private val json: Json = Json{ignoreUnknownKeys = true} // ignorar algun campo que no esta en nuestro pokemon
){
    //obtener el nombre del archivo Json que se va a leer
    private fun fileName(page: Int): String{
        val start = page * 10+1
        val end = (page +1) * 10

        fun p3(n: Int) = n.toString().padStart(3, '0') //agrega los 3 digitos para que sea el formato del nombre de los JSON
        return "pokemon_${p3(start)}_${p3(end)}" // formato de archivo
    }

    fun loadPokemons(page: Int): List<Pokemon>{
        val name = fileName(page) // obtenemos el nombre con la funcion filename
        val resId = context.resources.getIdentifier(name, "raw", context.packageName)
        if (resId == 0) return emptyList() // no existe el archivo

        val text = context.resources.openRawResource(resId).bufferedReader().use{it.readText()}
        return json.decodeFromString<List<Pokemon>>(text) //regresa la lista que se serizaliza de cada Json


    }
}
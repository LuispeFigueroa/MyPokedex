package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.remote.dih.NetworkModule
import com.uvg.mypokedex.data.remote.dto.toPokemon

class RepositoryImpl : PokemonRepository{

    private val api = NetworkModule.pokemonApi

    override suspend fun getPokemon(name: String): Result<Pokemon> {
        return try{
            //hace la llamada a la API para el pokemon
            val pokemonDto = api.getPokemonDetail(name)

            // si es exitosa se mapea al pokemon
            val pokemon = pokemonDto.toPokemon()
            // se guarda al pokemon con un wrap de succes, si no con un wrap de exception
            Result.success(pokemon)
            //si falla regresa exeption
        }catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
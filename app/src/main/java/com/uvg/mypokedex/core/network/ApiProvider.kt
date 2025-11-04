package com.uvg.mypokedex.core.network

import com.uvg.mypokedex.data.pokemon.remote.api.PokemonApiService

object ApiProvider {
    val pokemonApi: PokemonApiService by lazy { NetworkClient.pokemonRetrofit.create(PokemonApiService::class.java) }
}
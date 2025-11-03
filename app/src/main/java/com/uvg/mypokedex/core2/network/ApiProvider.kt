package com.uvg.mypokedex.core2.network

import com.uvg.mypokedex.data2.pokemon.remote.api.PokemonApiService

object ApiProvider {
    val pokemonApi: PokemonApiService by lazy { NetworkClient.pokemonRetrofit.create(PokemonApiService::class.java) }
}
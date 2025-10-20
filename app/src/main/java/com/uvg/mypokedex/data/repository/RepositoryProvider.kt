package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.data.remote.RemoteDataSourceImpl
import com.uvg.mypokedex.data.remote.api.PokemonApi
import com.uvg.mypokedex.data.remote.di.NetworkModule

object RepositoryProvider {
    val pokemonRepository: PokemonRepository by lazy {
        val api: PokemonApi = NetworkModule.pokemonApi
        val remote = RemoteDataSourceImpl(api)
        PokemonRepositoryImpl(remote)
    }
}

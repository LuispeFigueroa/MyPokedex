package com.uvg.mypokedex.data.remote.dih

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.uvg.mypokedex.data.remote.api.PokemonApi // Import your Api interface
import okhttp3.MediaType.Companion.toMediaType

object NetworkModule{

    // URL base para la API de pokemones
    private const val  BASE_URL = "https://pokeapi.co/api/v2/"

    //parser de JSON
    private val json = Json{
        ignoreUnknownKeys =true
    }


    private val okHttpClient = OkHttpClient.Builder()
        .build()

    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val pokemonApi: PokemonApi by lazy{
        retrofit.create(PokemonApi::class.java)
    }

}
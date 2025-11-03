package com.uvg.mypokedex.data2.pokemon.remote

import com.uvg.mypokedex.data2.pokemon.remote.api.PokemonApiService
import com.uvg.mypokedex.data2.pokemon.remote.dto.PokemonDto
import com.uvg.mypokedex.data2.pokemon.remote.dto.PokemonPageDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Clase para manejar la fuente de datos remotos (provenientes de la API)
class PokemonRemoteDataSource(
    private val api: PokemonApiService,
    private val io: CoroutineDispatcher = Dispatchers.IO
) {
    // Página de pokémon desde la API
    suspend fun fetchPage(limit: Int, offset: Int): PokemonPageDto = withContext(io) {
        api.getPokemonPage(limit = limit, offset = offset)
    }

    // Detalle de un pokémon
    suspend fun fetchDetail(id: Int): PokemonDto = withContext(io) {
        api.getPokemonDetail(id = id)
    }
}

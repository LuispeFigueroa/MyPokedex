package com.uvg.mypokedex.domain.repo

import com.uvg.mypokedex.domain.model.FavoritePokemon
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<FavoritePokemon>>
    fun observeIsFavorite(pokemonId: Int): Flow<Boolean>
    suspend fun addFavorite(pokemonId: Int, name: String): Result<Unit>
    suspend fun removeFavorite(pokemonId: Int): Result<Unit>
}
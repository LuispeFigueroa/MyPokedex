package com.uvg.mypokedex.domain.repo

import com.uvg.mypokedex.domain.model.Exchange
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    // Usuario A selecciona un pokemon y crea el Exchange
    suspend fun createExchangeWithOfferA(offerAId: Int, offerAName: String): Result<Exchange>

    // Usuario B se une al Exchange con el código
    suspend fun joinByCode(code: String): Result<Exchange>

    // Usuario B finaliza el trade
    suspend fun commitWithOfferB(exchangeId: String, offerBId: Int, offerBName: String): Result<Unit>

    // Función que observa los cambios en un Exchange
    fun observeExchange(exchangeId: String): Flow<Exchange?>
}
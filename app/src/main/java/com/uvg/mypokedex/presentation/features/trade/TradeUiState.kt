package com.uvg.mypokedex.presentation.features.trade

import com.uvg.mypokedex.domain.model.Exchange

data class TradeUiState(
    val isBusy: Boolean = false,
    val error: String? = null,
    val exchangeId: String? = null,
    val code: String? = null,
    val offerAId: Int? = null,
    val offerAName: String? = null,
    val status: Exchange.Status? = null
)
package com.uvg.mypokedex.presentation.features.trade

sealed class TradeEvent {
    data object TradeCompleted : TradeEvent()
}
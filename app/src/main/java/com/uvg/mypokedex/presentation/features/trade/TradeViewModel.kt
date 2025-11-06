package com.uvg.mypokedex.presentation.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.domain.model.Exchange
import com.uvg.mypokedex.domain.repo.ExchangeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TradeViewModel(
    private val exchangeRepo: ExchangeRepository
): ViewModel() {
    private val _state = MutableStateFlow(TradeUiState())
    val state: StateFlow<TradeUiState> = _state.asStateFlow()

    // Crear el trade (usuario A)
    fun createExchangeWithOfferA(id: Int, name: String) {
        viewModelScope.launch {
            _state.update { it.copy(isBusy = true, error = null) }
            val r = exchangeRepo.createExchangeWithOfferA(id, name)
            _state.update {
                r.fold(
                    onSuccess = { ex ->
                        it.copy(
                            isBusy = false,
                            exchangeId = ex.id,
                            code = ex.code,
                            offerAId = ex.offerAId,
                            offerAName = ex.offerAName,
                            status = ex.status
                        )
                    },
                    onFailure = { e -> it.copy(isBusy = false, error = e.message ?: "Failed") }
                )
            }
        }
    }

    // Unirse al trade con un código (usuario B)
    fun joinByCode(code: String, onJoined: (exchangeId: String) -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isBusy = true, error = null) }
            val r = exchangeRepo.joinByCode(code)
            _state.update {
                r.fold(
                    onSuccess = { ex ->
                        onJoined(ex.id)
                        it.copy(
                            isBusy = false,
                            exchangeId = ex.id,
                            code = ex.code,
                            offerAId = ex.offerAId,
                            offerAName = ex.offerAName,
                            status = ex.status
                        )
                    },
                    onFailure = { e -> it.copy(isBusy = false, error = e.message ?: "Invalid code") }
                )
            }
        }
    }

    // Finalizar trade (usuario B)
    fun commitWithOfferB(offerBId: Int, offerBName: String, onCommitted: () -> Unit) {
        val exId = _state.value.exchangeId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isBusy = true, error = null) }
            val r = exchangeRepo.commitWithOfferB(exId, offerBId, offerBName)
            _state.update { s ->
                r.fold(
                    onSuccess = { onCommitted(); s.copy(isBusy = false, status = Exchange.Status.COMMITTED) },
                    onFailure = { e -> s.copy(isBusy = false, error = e.message ?: "Commit failed") }
                )
            }
        }
    }
}
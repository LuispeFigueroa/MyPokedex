package com.uvg.mypokedex.presentation.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.domain.common.AppError
import com.uvg.mypokedex.domain.repo.PokemonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: PokemonRepository,
    private val id: Int
) : ViewModel() {
    private val _state = MutableStateFlow(DetailUiState(isLoading = true))
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    private var fetchJob: Job? = null
    private var triedFetch = false

    init {
        // Observar Room, si no hay dato, intentamos poblarlo desde la API
        viewModelScope.launch {
            repo.observeCachedDetail(id)
                .onStart { _state.update { it.copy(isLoading = true, error = null) } }
                .catch { t ->
                    val err = (t as? AppError) ?: AppError.Unknown(t)
                    _state.update { it.copy(isLoading = false, error = err) }
                }
                .collect { cached ->
                    if (cached != null) {
                        _state.update { it.copy(isLoading = false, pokemon = cached, error = null) }
                    } else {
                        if (!triedFetch) fetch() else _state.update { it.copy(isLoading = false) }
                    }
                }
        }
    }

    // Dispara la obtención desde la API y guarda en caché
    fun fetch() {
        if (fetchJob?.isActive == true) return
        triedFetch = true
        fetchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = repo.fetchAndCachePokemonDetail(id)
            _state.update { it.copy(isLoading = false, error = result.exceptionOrNull() as? AppError) }
        }
    }

    // Reintento manual si falló la red o no había caché
    fun retry() {
        _state.update { it.copy(error = null) }
        triedFetch = false
        fetch()
    }

    // Toggle de favoritos local
    fun toggleFavorite() {
        _state.update { it.copy(isFavorite = !it.isFavorite) }
    }

    // Limpiar errores
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
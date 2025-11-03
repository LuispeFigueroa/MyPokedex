package com.uvg.mypokedex.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data2.pokemon.constants.PokemonConstants.PAGE_SIZE
import com.uvg.mypokedex.data2.pokemon.prefs.SortOrder
import com.uvg.mypokedex.domain2.common.AppError
import com.uvg.mypokedex.domain2.repo.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: PokemonRepository
): ViewModel() {
    // Inicializamos y exponemos el estado de la UI
    private val _state = MutableStateFlow(
        HomeUiState(
            items = emptyList(),
            visibleCount = PAGE_SIZE,
            isLoadingInitial = true,
            isLoadingMore = false,
            error = null,
            endReached = false,
            order = SortOrder.ID_ASC
        )
    )
    val state: StateFlow<HomeUiState> = _state.asStateFlow()


    init {
        // Suscribirse a la lista completa en caché (ordenada por DataStore) y reflejarla en UI
        viewModelScope.launch {
            repo.observeCachedPokemon()
                .onStart {
                    // Solo la primera vez, muestra indicador de carga grande
                    _state.update { it.copy(isLoadingInitial = true, error = null) }
                }
                .catch { t ->
                    // Si el flujo falla, mostrar error y salir de "loading inicial"
                    val err = (t as? AppError) ?: AppError.Unknown(t)
                    _state.update { it.copy(isLoadingInitial = false, error = err) }
                }
                .collect { list ->
                    // Collect del flow, si llegan nuevos pokemons, la UI se actualiza sola
                    _state.update { s ->
                        s.copy(
                            items = list,
                            isLoadingInitial = false,
                            error = null
                        )
                    }
                }
        }

        // Suscribirse a los cambios de orden y reflejarlos en UI
        viewModelScope.launch {
            repo.orderPreference
                .distinctUntilChanged()
                .collect { ord ->
                    _state.update { it.copy(order = ord) }
                }
        }

        // Dispara la primera página al abrir la app
        viewModelScope.launch {
            loadMore()
        }
    }

    // Cambiar orden de la lista de pokemons
    fun changeOrder(newOrder: SortOrder) {
        viewModelScope.launch {
            repo.changeOrderPreference(newOrder)
        }
    }

    // Cargar más pokemones de la API
    fun loadMore() {
        viewModelScope.launch {
            val snapshot = state.value
            if (snapshot.isLoadingMore || snapshot.endReached) return@launch

            _state.update { it.copy(isLoadingMore = true, error = null) }

            val result = repo.fetchAndCacheNextPage()

            _state.update { st ->
                st.copy(
                    isLoadingMore = false,
                    error = result.exceptionOrNull() as? AppError
                ).let { s2 ->
                    // Marca fin de paginación si aplica
                    val end = result.exceptionOrNull() is AppError.EndOfPagination
                    if (end) s2.copy(endReached = true) else s2
                }
            }
        }
    }

    // Mostrar más pokemons de la caché (si es que hay)
    fun showMoreLocallyOrLoad() {
        val snapshot = state.value
        when {
            snapshot.hasMoreLocally -> {
                val newVisible = (snapshot.visibleCount + PAGE_SIZE).coerceAtMost(snapshot.items.size)
                _state.update { it.copy(visibleCount = newVisible) }
            }
            else -> {
                loadMore()
            }
        }
    }

    // Si loadMore() falla, el usuario puede reintentarlo
    fun retryLoadMore() {
        // Limpia error y vuelve a intentar
        _state.update { it.copy(error = null) }
        loadMore()
    }

    // Limpiar errores (para snackbars/banners)
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
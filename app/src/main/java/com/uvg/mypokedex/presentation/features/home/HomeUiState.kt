package com.uvg.mypokedex.presentation.features.home

import com.uvg.mypokedex.data.pokemon.constants.PokemonConstants.PAGE_SIZE
import com.uvg.mypokedex.data.pokemon.prefs.SortOrder
import com.uvg.mypokedex.domain.common.AppError
import com.uvg.mypokedex.domain.model.NamedItem

data class HomeUiState(
    val items: List<NamedItem> = emptyList(),
    val visibleCount: Int = PAGE_SIZE,
    val isLoadingInitial: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: AppError? = null,
    val endReached: Boolean = false,
    val order: SortOrder = SortOrder.ID_ASC
) {
    val isEmpty: Boolean get() = !isLoadingInitial && items.isEmpty() && error == null
    val hasMoreLocally: Boolean get() = items.size > visibleCount
}

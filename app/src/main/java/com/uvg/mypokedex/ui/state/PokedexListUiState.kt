package com.uvg.mypokedex.ui.state

import com.uvg.mypokedex.data.model.Pokemon

enum class SortOrder { BY_NAME_ASC, BY_NAME_DESC }

data class PokedexListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val query: String = "",
    val sortOrder: SortOrder = SortOrder.BY_NAME_ASC,
    val displayed: List<Pokemon> = emptyList(),
    val totalLoaded: Int = 0,
    val canLoadMore: Boolean = true
)
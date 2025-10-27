package com.uvg.mypokedex.ui.state

enum class SortField { BY_NUMBER, BY_NAME}
enum class SortOrder { ASC, DESC }

data class PokedexListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val query: String = "",
    val sortField: SortField = SortField.BY_NUMBER,
    val sortOrder: SortOrder = SortOrder.ASC,
    val canLoadMore: Boolean = true,
)
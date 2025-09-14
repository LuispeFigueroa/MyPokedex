package com.uvg.mypokedex.navigation

import com.uvg.mypokedex.data.model.Pokemon

sealed class Screen {
    data object HomeScreen : Screen()
    data class DetailScreen(val pokemon: Pokemon) : Screen()
    data object SearchToolsDialog : Screen()
}

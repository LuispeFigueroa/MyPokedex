package com.uvg.mypokedex.navigation


sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object DetailScreen : Screen("detail/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "detail/$pokemonId"
    }
    object SearchToolsDialog : Screen("searchTools")
}

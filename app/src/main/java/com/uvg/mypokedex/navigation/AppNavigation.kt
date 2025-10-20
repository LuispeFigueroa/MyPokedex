package com.uvg.mypokedex.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.features.details.DetailScreen
import com.uvg.mypokedex.ui.features.search.SearchToolsDialog
import com.uvg.mypokedex.ui.features.search.SortField
import com.uvg.mypokedex.ui.features.search.SortOrder

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.DetailScreen.createRoute(pokemonId))
                }
            )
        }

        composable(Screen.DetailScreen.route) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull()
            if (pokemonId == null) {
                Text("ID inválido")
                return@composable
            }

            DetailScreen(
                nameOrId = pokemonId,
                isFavorite = false,
                onToggleFavorite = { /* TODO favoritos */ },
                onBack = { navController.popBackStack() }
            )
        }

        dialog(Screen.SearchToolsDialog.route) {
            SearchToolsDialog(
                sortField = SortField.Numero,
                sortOrder = SortOrder.Ascendente,
                onSortFieldChange = { /* no requerido por el lab */ },
                onSortOrderChange = { /* no requerido por el lab */ },
                onApply = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
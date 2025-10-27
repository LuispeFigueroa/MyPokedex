package com.uvg.mypokedex.navigation

import android.app.Application
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.uvg.mypokedex.domain.sort.PokemonOrder
import com.uvg.mypokedex.ui.features.details.DetailScreen
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import com.uvg.mypokedex.ui.features.home.HomeViewModelFactory
import com.uvg.mypokedex.ui.features.search.SearchToolsDialog
import com.uvg.mypokedex.ui.state.SortField
import com.uvg.mypokedex.ui.state.SortOrder

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    favoriteIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit
) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            application = context.applicationContext as Application,
        )
    )

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                vm = homeViewModel,
                navController = navController,
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
                isFavorite = favoriteIds.contains(pokemonId),
                onToggleFavorite = { onToggleFavorite(pokemonId) },
                onBack = { navController.popBackStack() }
            )
        }

        dialog(Screen.SearchToolsDialog.route) {
            val uiState = homeViewModel.uiState.collectAsState().value
            SearchToolsDialog(
                sortField = uiState.sortField,
                sortOrder = uiState.sortOrder,
                onSortFieldChange = { homeViewModel.onSortFieldChanged(it) },
                onSortOrderChange = { homeViewModel.onSortOrderChanged(it) },
                onApply = {
                    val selectedOrder = when {
                        uiState.sortField == SortField.BY_NUMBER && uiState.sortOrder == SortOrder.ASC ->
                            PokemonOrder.NUMBER_ASC
                        uiState.sortField == SortField.BY_NUMBER && uiState.sortOrder == SortOrder.DESC ->
                            PokemonOrder.NUMBER_DESC
                        uiState.sortField == SortField.BY_NAME && uiState.sortOrder == SortOrder.ASC ->
                            PokemonOrder.NAME_ASC
                        uiState.sortField == SortField.BY_NAME && uiState.sortOrder == SortOrder.DESC ->
                            PokemonOrder.NAME_DESC
                        else -> PokemonOrder.NUMBER_ASC
                    }

                    homeViewModel.onOrderSelected(selectedOrder)

                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}




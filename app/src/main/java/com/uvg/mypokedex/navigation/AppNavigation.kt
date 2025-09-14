package com.uvg.mypokedex.navigation


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.features.details.DetailScreen
import com.uvg.mypokedex.ui.features.home.HomeViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    favoriteIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        // HomeScreen
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                favoriteIds = favoriteIds,
                onToggleFavorite = onToggleFavorite,
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.DetailScreen.createRoute(pokemonId))
                }
            )
        }

        // DetailScreen
        composable(Screen.DetailScreen.route) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull()

            // 👇 usamos el mismo ViewModel de HomeScreen
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.HomeScreen.route)
            }
            val homeViewModel: HomeViewModel = viewModel(parentEntry)

            val pokemon = homeViewModel.pokemonList.find { it.id == pokemonId }

            if (pokemon != null) {
                DetailScreen(
                    pokemon = pokemon,
                    isFavorite = favoriteIds.contains(pokemon.id),
                    onToggleFavorite = { onToggleFavorite(pokemon.id) },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Text("Cargando Pokémon...")
            }
        }
    }
}


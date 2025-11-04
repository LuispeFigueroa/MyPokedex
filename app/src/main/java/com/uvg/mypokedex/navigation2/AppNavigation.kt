package com.uvg.mypokedex.navigation2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uvg.mypokedex.core2.common.RepositoryProvider
import com.uvg.mypokedex.data2.pokemon.mapper.idFromUrl
import com.uvg.mypokedex.domain2.model.NamedItem
import com.uvg.mypokedex.presentation.features.detail.DetailScreen
import com.uvg.mypokedex.presentation.features.detail.DetailViewModel
import com.uvg.mypokedex.presentation.features.detail.DetailViewModelFactory
import com.uvg.mypokedex.presentation.features.home.HomeScreen
import com.uvg.mypokedex.presentation.features.home.HomeViewModel
import com.uvg.mypokedex.presentation.features.home.HomeViewModelFactory
import com.uvg.mypokedex.presentation.features.order.OrderSheet

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val repo = remember { RepositoryProvider.providePokemonRepository() }

    NavHost(
        navController = navController,
        startDestination = AppScreens.HOME
    ) {
        // HomeScreen
        composable(AppScreens.HOME) {
            val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(repo))

            HomeScreen(
                viewModel = homeVm,
                onItemClick = { item: NamedItem ->
                    val id = item.idFromUrl() ?: return@HomeScreen
                    navController.navigate(AppScreens.detail(id))
                },
                onOpenOrder = {
                    navController.navigate(AppScreens.ORDER)
                }
            )
        }

        // DetailScreen
        composable(
            route = AppScreens.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            val detailVm: DetailViewModel = viewModel(
                factory = DetailViewModelFactory(repo, id = id)
            )
            val state by detailVm.state.collectAsState()

            DetailScreen(
                viewModel = detailVm,
                onBack = { navController.popBackStack() }
            )
        }

        // Dialogo de opciones de orden
        dialog(AppScreens.ORDER) { backStackEntry ->
            // Reusamos el mismo HomeViewModel para leer/aplicar el orden
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppScreens.HOME)
            }
            val homeVm: HomeViewModel = viewModel(parentEntry)

            val currentOrder by homeVm.state.collectAsState()

            OrderSheet(
                current = currentOrder.order,
                onApply = { newOrder ->
                    homeVm.changeOrder(newOrder)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
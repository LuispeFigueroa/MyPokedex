package com.uvg.mypokedex.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uvg.mypokedex.core.common.RepositoryProvider
import com.uvg.mypokedex.data.pokemon.mapper.idFromUrl
import com.uvg.mypokedex.domain.model.NamedItem
import com.uvg.mypokedex.presentation.features.auth.AuthDialog
import com.uvg.mypokedex.presentation.features.detail.DetailScreen
import com.uvg.mypokedex.presentation.features.detail.DetailViewModel
import com.uvg.mypokedex.presentation.features.detail.DetailViewModelFactory
import com.uvg.mypokedex.presentation.features.home.HomeScreen
import com.uvg.mypokedex.presentation.features.home.HomeViewModel
import com.uvg.mypokedex.presentation.features.home.HomeViewModelFactory
import com.uvg.mypokedex.presentation.features.order.OrderSheet
import com.uvg.mypokedex.presentation.features.trade.TradeEnterCodeScreen
import com.uvg.mypokedex.presentation.features.trade.TradeEvent
import com.uvg.mypokedex.presentation.features.trade.TradeHomeScreen
import com.uvg.mypokedex.presentation.features.trade.TradeSelectScreen
import com.uvg.mypokedex.presentation.features.trade.TradeSelectViewModel
import com.uvg.mypokedex.presentation.features.trade.TradeSelectViewModelFactory
import com.uvg.mypokedex.presentation.features.trade.TradeViewModel
import com.uvg.mypokedex.presentation.features.trade.TradeViewModelFactory
import com.uvg.mypokedex.presentation.features.trade.TradeShowCodeScreen

// Clase de ayuda para la BottomBar
data class BottomItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val pokeRepo = remember { RepositoryProvider.providePokemonRepository() }
    val faveRepo = remember { RepositoryProvider.provideFavoritesRepository()  }
    val exchangeRepo = remember { RepositoryProvider.provideExchangeRepository() }
    val tradeVm: TradeViewModel = viewModel(
        factory = TradeViewModelFactory(
            exchangeRepo
        )
    )

    var showCompleted by remember { mutableStateOf(false) }

    // Collect one-time events globally
    LaunchedEffect(tradeVm) {
        tradeVm.events.collect { ev ->
            if (ev is TradeEvent.TradeCompleted) {
                showCompleted = true
            }
        }
    }

    val items = listOf(
        BottomItem(AppScreens.HOME, "Home", Icons.Filled.Home),
        BottomItem(AppScreens.TRADE, "Trade", Icons.Filled.Refresh),
    )
    val backStack by navController.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = current?.startsWith(item.route) == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Dialogo de trade completado
        if (showCompleted) {
            AlertDialog(
                onDismissRequest = {
                    showCompleted = false
                },
                title = { Text("Trade completed!") },
                text  = { Text("Both Pokémon were exchanged successfully.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showCompleted = false
                        }
                    ) { Text("OK") }
                }
            )
        }

        NavHost(
            navController = navController,
            startDestination = AppScreens.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            // HomeScreen
            composable(AppScreens.HOME) {
                val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(pokeRepo))

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
                    factory = DetailViewModelFactory(pokeRepo, id = id)
                )

                DetailScreen(
                    viewModel = detailVm,
                    onBack = { navController.popBackStack() },
                    onRequireLogin = { navController.navigate(AppScreens.AUTH) }
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

            // Dialogo de login
            dialog(AppScreens.AUTH) {
                AuthDialog(
                    onDismiss = { navController.popBackStack() },
                    onSignedIn = { navController.popBackStack() }
                )
            }

            // Pantalla principal de Trade
            composable(AppScreens.TRADE) { backStackEntry ->
                TradeHomeScreen(
                    viewModel = tradeVm,
                    onSelectFromFavorites = { navController.navigate(AppScreens.TRADE_SELECT) },
                    onEnterCode = { navController.navigate(AppScreens.TRADE_ENTER_CODE) }
                )
            }

            // Pantalla para seleccionar un pokemon para tradear
            composable(AppScreens.TRADE_SELECT) { backStackEntry ->
                val tradeSelectVm: TradeSelectViewModel = viewModel(
                    factory = TradeSelectViewModelFactory(faveRepo)
                )

                TradeSelectScreen(
                    viewModel = tradeSelectVm,
                    viewModelTrade = tradeVm,
                    onBack = { navController.popBackStack() },
                    onPokemonSelected = { id, name ->
                        // Si hay un exchangeId activo en VM, actúa como B (finalizar trade)
                        val exId = tradeVm.state.value.exchangeId
                        if (exId != null) {
                            tradeVm.commitWithOfferB(id, name) {
                                navController.popBackStack(AppScreens.TRADE, inclusive = false)
                            }
                        } else {
                            // Si no hay exchange activo, actúa como A (ir a ShowCode)
                            navController.navigate(AppScreens.showCode(id, name))
                        }
                    }
                )
            }

            // Pantalla que muestra código generado para tradear un pokemon
            composable(AppScreens.TRADE_SHOW_CODE) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull() ?: 0
                val name = backStackEntry.arguments?.getString("pokemonName") ?: "Unknown"

                TradeShowCodeScreen(
                    viewModel = tradeVm,
                    pokemonId = id,
                    pokemonName = name,
                    onBack = { navController.popBackStack(AppScreens.TRADE, inclusive = false) },
                    onDone = { navController.popBackStack(AppScreens.TRADE, inclusive = false) }
                )
            }

            // Pantalla para ingresar código para tradear un pokemon
            composable(AppScreens.TRADE_ENTER_CODE) { backStackEntry ->
                TradeEnterCodeScreen(
                    viewModel = tradeVm,
                    onBack = { navController.popBackStack() },
                    onJoinedGoSelect = { navController.navigate(AppScreens.TRADE_SELECT) }
                )
            }
        }
    }
}
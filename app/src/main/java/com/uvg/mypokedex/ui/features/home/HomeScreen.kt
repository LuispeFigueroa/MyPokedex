package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.uvg.mypokedex.navigation.Screen
import com.uvg.mypokedex.ui.components.PokemonCard
import com.uvg.mypokedex.ui.state.SortField
import com.uvg.mypokedex.ui.state.SortOrder
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel,
    navController: NavController,
    onPokemonClick: (Int) -> Unit
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val list = vm.pokemonList
    var query by remember { mutableStateOf(TextFieldValue("")) }
    val isConnected by vm.isConnected.collectAsState()

    val gridState = rememberLazyGridState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            snackbarHostState.showSnackbar(
                message = "Sin conexión. Modo offline.",
                withDismissAction = false
            )
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("My Pokedex") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.SearchToolsDialog.route) }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Ordenar")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar por nombre") },
                singleLine = true
            )

            when {
                list.isEmpty() && uiState.isLoading -> FullScreenLoading()
                list.isEmpty() && uiState.errorMessage != null -> {
                    FullScreenError(
                        message = uiState.errorMessage!!,
                        onRetry = { vm.loadMorePokemon() }
                    )
                }
                else -> {
                    val sortedList = remember(list.size, uiState.sortField, uiState.sortOrder) {
                        when (uiState.sortField) {
                            SortField.BY_NUMBER -> {
                                if (uiState.sortOrder == SortOrder.ASC) list.sortedBy { it.id }
                                else list.sortedByDescending { it.id }
                            }
                            SortField.BY_NAME -> {
                                if (uiState.sortOrder == SortOrder.ASC) list.sortedBy { it.name }
                                else list.sortedByDescending { it.name }
                            }
                        }
                    }

                    val filteredList = if (query.text.isBlank()) sortedList
                    else sortedList.filter { it.name.contains(query.text, ignoreCase = true) }

                    LazyVerticalGrid(
                        state = gridState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        columns = GridCells.Adaptive(minSize = 160.dp)
                    ) {
                        items(items = filteredList, key = { it.id }) { pokemon ->
                            PokemonCard(pokemon = pokemon, onClick = { onPokemonClick(pokemon.id) })
                        }

                        if (uiState.isLoading && list.isNotEmpty()) {
                            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                                PagingLoadingIndicator()
                            }
                        }

                        if (uiState.errorMessage != null && list.isNotEmpty()) {
                            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                                PagingError(
                                    message = uiState.errorMessage!!,
                                    onRetry = { vm.loadMorePokemon() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .map { it ?: 0 }
            .distinctUntilChanged()
            .filter { lastVisibleIndex ->
                val totalItems = gridState.layoutInfo.totalItemsCount
                totalItems > 0 && lastVisibleIndex >= totalItems - 10
            }
            .collect {
                if (!uiState.isLoading && uiState.canLoadMore) {
                    vm.loadMorePokemon()
                }
            }
    }

    LaunchedEffect(Unit) {
        if (list.isEmpty()) {
            vm.loadMorePokemon()
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FullScreenError(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(message)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) { Text("Reintentar") }
        }
    }
}

@Composable
private fun PagingLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PagingError(message: String, onRetry: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.width(8.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}


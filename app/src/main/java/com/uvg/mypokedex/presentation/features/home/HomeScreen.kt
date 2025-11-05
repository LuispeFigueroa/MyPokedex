package com.uvg.mypokedex.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvg.mypokedex.core.common.ErrorMessageProvider
import com.uvg.mypokedex.core.common.RepositoryProvider
import com.uvg.mypokedex.core.network.monitor.NetworkMonitor
import com.uvg.mypokedex.domain.model.NamedItem
import com.uvg.mypokedex.presentation.components.ErrorBanner
import com.uvg.mypokedex.presentation.components.PokemonCard
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onItemClick: (NamedItem) -> Unit = {},
    onOpenOrder: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val networkMonitor: NetworkMonitor = RepositoryProvider.provideNetworkMonitor()
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(initialValue = true)
    var query by rememberSaveable { mutableStateOf("") }
    val gridState = rememberLazyGridState()

    val filteredItems = remember(state.items, query) {
        if (query.isBlank()) state.items
        else state.items.filter { it.name.contains(query.trim(), ignoreCase = true) }
    }

    val visibleFiltered = remember(filteredItems, state.visibleCount) {
        filteredItems.take(state.visibleCount)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("MyPokedex") },
                actions = {
                    if (!isOnline) {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { Text("No Internet connection") },
                            state = rememberTooltipState()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "No Internet connection",
                                tint = Color.Red,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                    IconButton(onClick = { onOpenOrder() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Order",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                singleLine = true,
                label = { Text("Search by name") },
                placeholder = { Text("Ex. Pikachu") }
            )

            // Lista de pokemones
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Banner de error (si hay)
                state.error?.let { appErr ->
                    if (state.items.isEmpty()){
                        ErrorBanner(
                            message = ErrorMessageProvider.userMessage(appErr),
                            dismissButton = false,
                            onDismiss = { viewModel.clearError() },
                            onRetry = { viewModel.retryLoadMore() },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(0.85f)
                                .zIndex(1f)
                        )
                    } else {
                        ErrorBanner(
                            message = ErrorMessageProvider.userMessage(appErr),
                            onDismiss = { viewModel.clearError() },
                            onRetry = { viewModel.retryLoadMore() },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(0.85f)
                                .zIndex(1f)
                        )
                    }
                }

                // Lista de pokemones (usa visibleItems para paginado local)
                if (visibleFiltered.isNotEmpty()) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        state = gridState
                    ) {
                        items(visibleFiltered, key = { it.name }) { item ->
                            PokemonCard(
                                item = item,
                                onClick = { onItemClick(item) }
                            )
                        }

                        item(
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                when {
                                    state.isLoadingMore -> CircularProgressIndicator()
                                    state.endReached     -> Text("No more pokemon left")
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }

                // Loader inicial
                if (state.isLoadingInitial) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Vacío
                if (state.isEmpty && state.error == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    // Umbral de estar "cerca del final" de la página
    val prefetchThreshold = 6

    // Detector de estar "cerca del final" de la página
    LaunchedEffect(
        gridState,
        state.isLoadingMore,
        state.endReached,
        state.isLoadingInitial,
        visibleFiltered.size,
    ) {
        snapshotFlow {
            val info = gridState.layoutInfo
            val lastVisibleIndex = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            val total = info.totalItemsCount
            lastVisibleIndex to total
        }
            .map { (lastIndex, total) ->
                lastIndex >= 0 && total > 0 && lastIndex >= total - prefetchThreshold
            }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                if (!state.isLoadingMore && !state.endReached && !state.isLoadingInitial) {
                    viewModel.showMoreLocallyOrLoad()
                }
            }
    }
}
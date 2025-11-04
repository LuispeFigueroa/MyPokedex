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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvg.mypokedex.core2.common.ErrorMessageProvider
import com.uvg.mypokedex.domain2.model.NamedItem
import com.uvg.mypokedex.presentation.components.ErrorBanner
import com.uvg.mypokedex.presentation.components.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onItemClick: (NamedItem) -> Unit = {},
    onOpenOrder: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }
    var showOrderDialog by remember { mutableStateOf(false) }

    val filteredItems = remember(state.items, query) {
        if (query.isBlank()) state.items
        else state.items.filter { it.name.contains(query.trim(), ignoreCase = true) }
    }

    val visibleFiltered = remember(filteredItems, state.visibleCount) {
        filteredItems.take(state.visibleCount)
    }

    val hasMoreLocallyFiltered = filteredItems.size > state.visibleCount

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("MyPokedex") },
                actions = {
                    IconButton(onClick = { onOpenOrder() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Ordenar"
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
                label = { Text("Buscar por nombre") },
                placeholder = { Text("Ej. pikachu") }
            )

            // Lista de pokemones
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Banner de error (si hay)
                state.error?.let { appErr ->
                    ErrorBanner(
                        message = ErrorMessageProvider.userMessage(appErr),
                        onDismiss = { viewModel.clearError() },
                        onRetry = { viewModel.retryLoadMore() },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(12.dp)
                    )
                }

                // Lista de pokemones (usa visibleItems para paginado local)
                if (visibleFiltered.isNotEmpty()) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                        columns = GridCells.Adaptive(minSize = 160.dp)
                    ) {
                        items(visibleFiltered, key = { it.name }) { item ->
                            PokemonCard(
                                item = item,
                                onClick = { onItemClick(item) }
                            )
                        }

                        item {
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val label = when {
                                    state.isLoadingMore -> "Cargando…"
                                    state.endReached -> "No hay más pokemon"
                                    hasMoreLocallyFiltered -> "Cargar más"
                                    else -> "Cargar más"
                                }
                                Button(
                                    onClick = { viewModel.showMoreLocallyOrLoad() },
                                    enabled = !state.isLoadingMore && !state.endReached
                                ) { Text(label) }
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
                if (state.isEmpty) {
                    Text(
                        text = "Aún no hay resultados",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
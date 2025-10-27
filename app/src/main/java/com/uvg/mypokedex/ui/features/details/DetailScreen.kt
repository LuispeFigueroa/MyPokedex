package com.uvg.mypokedex.ui.features.details

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.uvg.mypokedex.ui.components.PokemonMeasurements
import com.uvg.mypokedex.ui.components.PokemonStatRow
import com.uvg.mypokedex.ui.components.StatType
import com.uvg.mypokedex.ui.components.TopBar

@Composable
fun DetailScreen(
    nameOrId: Int,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val vm: PokemonDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = PokemonDetailViewModelFactory(
            application = context.applicationContext as Application
        )
    )
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(nameOrId) {
        vm.load(nameOrId.toString())
    }

    Scaffold(
        topBar = {
            TopBar(
                name = state.pokemon?.name ?: "Pokemon #$nameOrId",
                isFavorite = isFavorite,
                onToggleFavorite = onToggleFavorite,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) { CircularProgressIndicator() }
            }
            state.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(state.errorMessage!!)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { vm.load(nameOrId.toString()) }) { Text("Reintentar") }
                }
            }
            state.pokemon != null -> {
                val p = state.pokemon!!
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${p.id}.png",
                        contentDescription = p.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.height(12.dp))
                    PokemonMeasurements(pokemon = p)
                    Spacer(Modifier.height(12.dp))
                    for (stat in StatType.entries) {
                        PokemonStatRow(pokemon = p, stat = stat)
                    }
                }
            }
        }
    }
}

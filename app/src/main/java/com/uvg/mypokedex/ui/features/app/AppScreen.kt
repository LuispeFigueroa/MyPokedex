package com.uvg.mypokedex.ui.features.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.uvg.mypokedex.ui.features.details.DetailScreen
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.uvg.mypokedex.data.model.Pokemon

@Composable
fun AppScreen(viewModel: HomeViewModel = HomeViewModel()) {
    var selected by remember { mutableStateOf<Pokemon?>(null) }

    if (selected == null) {
        HomeScreen(
            viewModel = viewModel,
            onPokemonClick = { p -> selected = p }
        )
    } else {
        DetailScreen(
            pokemon = selected!!,
            onBackClick = { selected = null }
        )
    }
}
package com.uvg.mypokedex.ui.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.theme.MyPokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPokedexTheme {
                var favoriteIds by rememberSaveable { mutableStateOf(setOf<Int>()) }

                fun toggleFavorite(id: Int) {
                    favoriteIds = favoriteIds.toMutableSet().apply {
                        if (contains(id)) remove(id) else add(id)
                    }
                }
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                    ){
                        HomeScreen(
                            favoriteIds = favoriteIds,
                            onToggleFavorite = { id -> toggleFavorite(id) },
                        )
                    }
            }
        }
    }
}
}
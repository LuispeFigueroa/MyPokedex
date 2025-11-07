package com.uvg.mypokedex.ui.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.uvg.mypokedex.navigation.AppNavigation
import com.uvg.mypokedex.ui.theme.MyPokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPokedexTheme {
                val favoriteIds = rememberSaveable { mutableStateListOf<Int>() }

                fun toggleFavorite(id: Int) {
                    if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
                }
                AppNavigation(
                    favoriteIds = favoriteIds.toSet(),
                    onToggleFavorite = { id -> toggleFavorite(id) }
                )
            }
        }
    }
}

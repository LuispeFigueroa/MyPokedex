package com.uvg.mypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.uvg.mypokedex.core2.common.RepositoryProvider
import com.uvg.mypokedex.navigation.AppNavigation
import com.uvg.mypokedex.presentation.features.home.HomeScreen
import com.uvg.mypokedex.presentation.features.home.HomeViewModel
import com.uvg.mypokedex.ui.theme.MyPokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RepositoryProvider.init(application)
        val repo = RepositoryProvider.providePokemonRepository()
        val monitor = RepositoryProvider.provideNetworkMonitor()
        val vm = HomeViewModel(repo)
        enableEdgeToEdge()
        setContent {
            MyPokedexTheme {
                /*
                val favoriteIds = rememberSaveable { mutableStateListOf<Int>() }

                fun toggleFavorite(id: Int) {
                    if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
                }
                */
                HomeScreen(vm)
            }
        }
    }
}
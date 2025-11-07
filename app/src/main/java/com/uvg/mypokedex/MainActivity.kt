package com.uvg.mypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uvg.mypokedex.core.common.RepositoryProvider
import com.uvg.mypokedex.navigation.AppNavigation
import com.uvg.mypokedex.presentation.theme.MyPokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RepositoryProvider.init(application)
        enableEdgeToEdge()
        setContent {
            MyPokedexTheme {
                AppNavigation()
            }
        }
    }
}
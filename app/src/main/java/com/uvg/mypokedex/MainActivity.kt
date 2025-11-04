package com.uvg.mypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uvg.mypokedex.core2.common.RepositoryProvider
import com.uvg.mypokedex.navigation2.AppNavigation
import com.uvg.mypokedex.ui.theme.MyPokedexTheme

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
package com.uvg.mypokedex.ui.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.theme.MyPokedexTheme
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats
import com.uvg.mypokedex.ui.features.details.DetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPokedexTheme {
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                    ){
                        HomeScreen()
                    }
            }
        }
    }
}
}
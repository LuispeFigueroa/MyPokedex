package com.uvg.mypokedex.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TopBar(
    name: String,
    isFavorite: Boolean,
    onToggleFavorite: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(name) },
        navigationIcon = {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back icon"
            )
        },
        actions = {
            if (onToggleFavorite != null) {
                IconButton(onClick = onToggleFavorite) {
                    if (isFavorite) Icon(Icons.Default.Favorite, contentDescription = "Unlike")
                    else Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
                }
            }
        }
    )
}

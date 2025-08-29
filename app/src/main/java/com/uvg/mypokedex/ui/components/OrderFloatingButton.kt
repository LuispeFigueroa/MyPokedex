package com.uvg.mypokedex.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun OrderButton(initialIsAscending: Boolean = true) {
    var isAscending by remember { mutableStateOf(initialIsAscending) } //establecer estado inicial

    FloatingActionButton(
        onClick = {isAscending = !isAscending }, //cambiar entre orden ascendente y descendente
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        val iconShowing = if (isAscending) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
        val description = if (isAscending) "Ordenar de A-Z" else "Ordenar de Z-A"
        Icon(
            imageVector = iconShowing,
            contentDescription = description

        )
    }
}
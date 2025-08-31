package com.uvg.mypokedex.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun OrderButton(
                onClick:() -> Unit,
                isCurrentlyAscending: Boolean) {

    FloatingActionButton(
        onClick = onClick,//cambiar entre orden ascendente y descendente
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        val iconShowing = if (isCurrentlyAscending) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
        val description = if (isCurrentlyAscending) "Ordenar de A-Z" else "Ordenar de Z-A"
        Icon(
            imageVector = iconShowing,
            contentDescription = description

        )
    }
}
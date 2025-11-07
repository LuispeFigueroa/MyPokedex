package com.uvg.mypokedex.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorBanner(
    modifier: Modifier = Modifier,
    message: String,
    retryButton: Boolean = true,
    dismissButton: Boolean = true,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            if (retryButton) {
                onRetry?.let {
                    TextButton(onClick = it) { Text("Retry") }
                }
            }

            if (dismissButton) {
                onDismiss?.let {
                    TextButton(onClick = it) { Text("Close") }
                }
            }
        }
    }
}

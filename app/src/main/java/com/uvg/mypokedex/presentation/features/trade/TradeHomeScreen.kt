package com.uvg.mypokedex.presentation.features.trade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvg.mypokedex.domain.model.Exchange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeHomeScreen(
    viewModel: TradeViewModel,
    onSelectFromFavorites: () -> Unit,
    onEnterCode: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val hasActive = state.value.exchangeId != null && state.value.status != Exchange.Status.COMMITTED
            && state.value.status != Exchange.Status.CANCELLED && state.value.status != Exchange.Status.EXPIRED

    Scaffold(
        topBar = { MediumTopAppBar(title = { Text("Trade") }) }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .let {
                        // Si hay trade activo, se deshabilita
                        it.clickable(enabled = !hasActive) { onSelectFromFavorites() }
                    }
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Select Pokemon", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    if (hasActive) {
                        Text(
                            text = "You already have a trade in progress",
                            color = Color.Red,
                        )
                    } else {
                        Text("Pick one of your favorites to trade.")
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .let {
                        // Si hay trade activo, se deshabilita
                        it.clickable(enabled = !hasActive) { onEnterCode() }
                    }
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Enter code", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    if (hasActive) {
                        Text(
                            text = "You already have a trade in progress",
                            color = Color.Red,
                        )
                    } else {
                        Text("Enter friend's exchange code.")
                    }
                }
            }

            if (hasActive) {
                Spacer(Modifier.height(8.dp))
                androidx.compose.material3.Button(
                    onClick = { viewModel.cancelActive() },
                    enabled = !state.value.isBusy
                ) {
                    Text("Cancel active trade")
                }
                state.value.error?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
                if (state.value.isBusy) {
                    Spacer(Modifier.height(4.dp))
                    Text("Processing…")
                }
            }
        }
    }
}
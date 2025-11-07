package com.uvg.mypokedex.presentation.features.auth

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthDialog(
    onDismiss: () -> Unit,
    onSignedIn: () -> Unit,
    vm: AuthViewModel = viewModel(factory = AuthViewModelFactory())
) {
    val state by vm.state.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sign in required") },
        text = {
            when {
                state.isSigningIn -> {
                    Text("Signing you in…")
                }
                state.error != null -> {
                    Text(
                        text = state.error!!,
                        color = Color.Red
                    )
                }
                else -> {
                    Text("To favorite a Pokémon you must sign in. Continue?")
                }
            }
        },
        confirmButton = {
            if (state.isSigningIn) {
                CircularProgressIndicator()
            } else {
                Button(onClick = {
                    vm.signInAnonymously()
                }) {
                    Text("Sign in anonimously")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )

    // Cerrar el dialogo si el usuario se ha logueado
    if (!state.isSigningIn && vm.isSignedIn()) {
        LaunchedEffect("signed-in") { onSignedIn() }
    }
}
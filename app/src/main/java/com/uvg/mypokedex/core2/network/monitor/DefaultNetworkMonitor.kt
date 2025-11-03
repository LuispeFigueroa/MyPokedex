package com.uvg.mypokedex.core2.network.monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

// Implementación del monitor de conexión a internet
class DefaultNetworkMonitor(
    context: Context
): NetworkMonitor {
    // Se obtiene el ConnectivityManager
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Flow que devuelve un boolean cada vez que el estado de conexión cambia
    override val isOnline: Flow<Boolean> = callbackFlow {
        // Se envía el dato de conexión inicial
        trySend(hasInternet())

        // Se define el callback para que el sistema monitoree y notifique de cualquier cambio en la conexión
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(hasInternet())
            }

            override fun onLost(network: Network) {
                trySend(hasInternet())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(hasInternet())
            }
        }

        // Se inicializa el callback
        cm.registerDefaultNetworkCallback(callback)
        awaitClose { cm.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    // Función que revisa si el dispositivo esta realmente conectado a internet
    private fun hasInternet(): Boolean {
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        // VALIDATED implica salida a Internet
        val validated = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        val hasTransport = caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        return validated && hasTransport
    }
}
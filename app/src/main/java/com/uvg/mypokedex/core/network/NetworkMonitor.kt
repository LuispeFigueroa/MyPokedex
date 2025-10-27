package com.uvg.mypokedex.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkMonitor(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private fun checkNow(): Boolean {
        val activeNetwork: Network? = connectivityManager.activeNetwork
        val caps: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(activeNetwork)

        return caps?.let { nc ->
            nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } == true
    }

    val isConnected: Flow<Boolean> = callbackFlow {
        trySend(checkNow())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(checkNow())
            }

            override fun onLost(network: Network) {
                trySend(checkNow())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(checkNow())
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}

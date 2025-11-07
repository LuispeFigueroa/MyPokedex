package com.uvg.mypokedex.core.network.monitor

import kotlinx.coroutines.flow.Flow

// Interfaz del monitor de conexión a internet
interface NetworkMonitor {
    val isOnline: Flow<Boolean> // Devuelve true si hay conexión, false si no
}
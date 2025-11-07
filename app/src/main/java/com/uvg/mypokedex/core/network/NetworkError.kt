package com.uvg.mypokedex.core.network

sealed class NetworkError {
    data object NoInternet : NetworkError()
    data class Http(val code: Int) : NetworkError()
    data object Timeout : NetworkError()
    data object Unknown : NetworkError()
}
package com.uvg.mypokedex.core.network

import java.io.IOException
import java.net.SocketTimeoutException
import retrofit2.HttpException

/** Mapea Throwable -> NetworkError + mensaje amigable para el usuario. */
object ErrorMapper {

    data class UiError(
        val error: NetworkError,
        val message: String
    )

    fun toUiError(t: Throwable): UiError {
        return when (t) {
            is SocketTimeoutException -> UiError(
                NetworkError.Timeout,
                "La conexión tardó demasiado. Intenta nuevamente."
            )
            is IOException -> UiError(
                NetworkError.NoInternet,
                "Sin conexión a Internet. Verifica tu red e intenta de nuevo."
            )
            is HttpException -> {
                val code = t.code()
                val userMessage = when (code) {
                    in 500..599 -> "Servidor con problemas. Intenta más tarde."
                    404 -> "No encontrado."
                    401, 403 -> "No autorizado."
                    else -> "Error del servidor ($code)."
                }
                UiError(NetworkError.Http(code), userMessage)
            }
            else -> UiError(
                NetworkError.Unknown,
                "Ocurrió un error inesperado."
            )
        }
    }
}

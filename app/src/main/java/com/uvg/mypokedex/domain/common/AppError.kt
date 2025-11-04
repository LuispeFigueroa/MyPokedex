package com.uvg.mypokedex.domain.common

sealed class AppError(message: String? = null, cause: Throwable? = null) : Throwable(message, cause) {
    // Errores esperables para UI
    object Offline : AppError()
    object Timeout : AppError()
    data class Server(val code: Int, val body: String? = null) : AppError()
    object NotFound : AppError()
    object Unauthorized : AppError()
    object Forbidden : AppError()
    object EndOfPagination : AppError()

    // Otros
    object Parsing : AppError()
    data class Unknown(val original: Throwable) : AppError(original.message, original)
}
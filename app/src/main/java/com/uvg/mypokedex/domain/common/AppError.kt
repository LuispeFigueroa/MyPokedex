package com.uvg.mypokedex.domain.common

sealed class AppError(message: String? = null, cause: Throwable? = null) : Throwable(message, cause) {
    // Errores esperables para UI
    object Offline : AppError() {
        private fun readResolve(): Any = Offline
    }

    object Timeout : AppError() {
        private fun readResolve(): Any = Timeout
    }

    data class Server(val code: Int, val body: String? = null) : AppError()
    object NotFound : AppError() {
        private fun readResolve(): Any = NotFound
    }

    object Unauthorized : AppError() {
        private fun readResolve(): Any = Unauthorized
    }

    object Forbidden : AppError() {
        private fun readResolve(): Any = Forbidden
    }

    object EndOfPagination : AppError() {
        private fun readResolve(): Any = EndOfPagination
    }

    // Otros
    object Parsing : AppError() {
        private fun readResolve(): Any = Parsing
    }

    data class Unknown(val original: Throwable) : AppError(original.message, original)
}
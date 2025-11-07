package com.uvg.mypokedex.core.network

import com.uvg.mypokedex.domain.common.AppError
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toAppError(): AppError = when (this) {
    is SocketTimeoutException -> AppError.Timeout
    is IOException -> AppError.Offline
    is HttpException -> when (code()) {
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.NotFound
        in 500..599 -> AppError.Server(code(), message())
        else -> AppError.Server(code(), message())
    }
    is SerializationException -> AppError.Parsing
    else -> AppError.Unknown(this)
}
package com.uvg.mypokedex.core2.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> T
): Result<T> = withContext(dispatcher) {
    try {
        Result.success(block())
    } catch (t: Throwable) {
        Result.failure(t.toAppError())
    }
}

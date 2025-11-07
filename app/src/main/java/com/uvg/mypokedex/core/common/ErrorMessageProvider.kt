package com.uvg.mypokedex.core.common

import com.uvg.mypokedex.domain.common.AppError

object ErrorMessageProvider {
    fun userMessage(error: AppError): String = when (error) {
        AppError.Offline         -> "No internet connection."
        AppError.Timeout         -> "Connection timed out. Try again."
        is AppError.Server       -> "Server error (${error.code}). Try again."
        AppError.NotFound        -> "Could not find what you were looking for."
        AppError.Unauthorized    -> "Unauthorized session. Log in again."
        AppError.Forbidden       -> "Forbidden action."
        AppError.EndOfPagination -> "No more pokemon to show."
        AppError.Parsing         -> "An error ocurred while parsing the response. Try again."
        is AppError.Unknown      -> "Unexpected error. Try again."
    }
}
package com.uvg.mypokedex.core2.common

import com.uvg.mypokedex.domain2.common.AppError

object ErrorMessageProvider {
    fun userMessage(error: AppError): String = when (error) {
        AppError.Offline         -> "Sin conexión a Internet. Revisa tu red e inténtalo de nuevo."
        AppError.Timeout         -> "La conexión tardó demasiado. Intenta nuevamente."
        is AppError.Server       -> "Error del servidor (${error.code}). Intenta más tarde."
        AppError.NotFound        -> "No encontramos lo que buscabas."
        AppError.Unauthorized    -> "Tu sesión no está autorizada. Vuelve a iniciar sesión."
        AppError.Forbidden       -> "No tienes permisos para esta acción."
        AppError.EndOfPagination -> "No hay más pokemons para mostrar."
        AppError.Parsing         -> "Hubo un problema leyendo los datos. Intenta de nuevo."
        is AppError.Unknown      -> "Ocurrió un error inesperado. Intenta nuevamente."
    }
}
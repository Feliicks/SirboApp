package com.felicks.sirbo.data.models

import com.felicks.sirbo.data.models.error.ErrorDetails

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()  // Respuesta exitosa
    data class Error(val error: ErrorDetails) : ApiResult<Nothing>()  // Error de API
    object Loading : ApiResult<Nothing>()  // Estado de carga
    object Empty : ApiResult<Nothing>()  // Sin datos
}
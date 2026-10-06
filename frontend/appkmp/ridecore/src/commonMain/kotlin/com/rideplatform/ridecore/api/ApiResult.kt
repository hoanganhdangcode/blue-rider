package com.rideplatform.ridecore.api

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Failure(val code: Int?, val message: String) : ApiResult<Nothing>
}

class ApiException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

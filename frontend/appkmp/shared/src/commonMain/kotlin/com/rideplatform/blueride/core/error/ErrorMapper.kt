package com.rideplatform.blueride.core.error

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

object ErrorMapper {
    fun map(throwable: Throwable): AppError = when (throwable){
        is ClientRequestException -> AppError.Client(throwable.response.status.value, throwable.message)
        is ServerResponseException -> AppError.Server(throwable.response.status.value, throwable.message)
        is ResponseException -> AppError.Unknown(throwable.message, throwable)
        is IOException -> AppError.Network(throwable.message, throwable)
        is SerializationException -> AppError.Unknown("Serialization error: ${throwable.message}", throwable)
        else -> AppError.Unknown(throwable.message, throwable)

    }
}
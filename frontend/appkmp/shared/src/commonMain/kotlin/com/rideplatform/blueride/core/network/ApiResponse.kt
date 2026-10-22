package com.rideplatform.blueride.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val code: String,
    val data: T? = null,
    val message: String? = null,
)
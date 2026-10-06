package com.rideplatform.ridecore.api

data class ApiConfig(
    val baseUrl: String,
    val connectTimeoutMs: Long = 10_000,
    val requestTimeoutMs: Long = 30_000,
    val authTokenProvider: suspend () -> String? = { null },
)

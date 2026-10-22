package com.rideplatform.ridecore.api

interface ApiClient {
    suspend fun <T> get(path: String, decode: (String) -> T): ApiResult<T>
    suspend fun <T> post(path: String, body: String, decode: (String) -> T): ApiResult<T>
}

class HttpApiClient(private val config: ApiConfig) : ApiClient {

    override suspend fun <T> get(path: String, decode: (String) -> T): ApiResult<T> =
        ApiResult.Failure(code = null, message = "GET $path not implemented yet")

    override suspend fun <T> post(path: String, body: String, decode: (String) -> T): ApiResult<T> =
        ApiResult.Failure(code = null, message = "POST $path not implemented yet")
}

package com.rideplatform.blueride.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    val baseUrl: String,
    val enableHttpLog: Boolean,
    val debug: Boolean
) {
    fun create(): HttpClient {
        return HttpClient {
            expectSuccess = true // nếu có lỗi thì throw exception
            install(ContentNegotiation){
                json(Json{
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = false // cho phép JSON sai chuẩn vd "name": abc ~ "name":"abc"
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = if (enableHttpLog) LogLevel.ALL else LogLevel.NONE
                if (!debug) sanitizeHeader { h ->( h.equals(HttpHeaders.Authorization, ignoreCase = true) || h.equals(HttpHeaders.Cookie,ignoreCase = true)) } //bỏ author khỏi log
            }
            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json)
            }
        }
    }
}

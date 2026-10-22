package com.rideplatform.blueride.core.error

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ErrorMapperTest {

    // region HTTP status (exception thật do Ktor ném khi expectSuccess = true)

    @Test
    fun badRequest_mapsToClient() = runTest {
        val error = ErrorMapper.map(exceptionForStatus(HttpStatusCode.BadRequest))

        assertIs<AppError.Client>(error)
        assertEquals(400, error.httpStatus)
    }

    @Test
    fun notFound_mapsToClient() = runTest {
        val error = ErrorMapper.map(exceptionForStatus(HttpStatusCode.NotFound))

        assertIs<AppError.Client>(error)
        assertEquals(404, error.httpStatus)
    }

    @Test
    fun internalServerError_mapsToServer() = runTest {
        val error = ErrorMapper.map(exceptionForStatus(HttpStatusCode.InternalServerError))

        assertIs<AppError.Server>(error)
        assertEquals(500, error.httpStatus)
    }

    @Test
    fun serviceUnavailable_mapsToServer() = runTest {
        val error = ErrorMapper.map(exceptionForStatus(HttpStatusCode.ServiceUnavailable))

        assertIs<AppError.Server>(error)
        assertEquals(503, error.httpStatus)
    }

    @Test
    fun redirect_mapsToUnknown() = runTest {
        val cause = exceptionForStatus(HttpStatusCode.Found)

        val error = ErrorMapper.map(cause)

        assertIs<AppError.Unknown>(error)
        assertSame(cause, error.throwable)
    }

    // endregion

    // region Network

    @Test
    fun requestTimeout_mapsToNetwork() {
        assertIs<AppError.Network>(ErrorMapper.map(HttpRequestTimeoutException("https://test/", 1_000)))
    }

    @Test
    fun connectTimeout_mapsToNetwork() {
        assertIs<AppError.Network>(ErrorMapper.map(ConnectTimeoutException("connect timeout")))
    }

    @Test
    fun socketTimeout_mapsToNetwork() {
        assertIs<AppError.Network>(ErrorMapper.map(SocketTimeoutException("socket timeout")))
    }

    @Test
    fun ioException_mapsToNetwork_andKeepsCause() {
        val cause = IOException("no route to host")

        val error = ErrorMapper.map(cause)

        assertIs<AppError.Network>(error)
        assertSame(cause, error.throwable)
    }

    // endregion

    // region Unknown

    @Test
    fun serializationException_mapsToUnknown_withPrefixedMessage() {
        val error = ErrorMapper.map(SerializationException("bad json"))

        assertIs<AppError.Unknown>(error)
        assertTrue(error.message!!.startsWith("Serialization error"))
    }

    @Test
    fun otherException_mapsToUnknown_andKeepsCause() {
        val cause = IllegalStateException("boom")

        val error = ErrorMapper.map(cause)

        assertIs<AppError.Unknown>(error)
        assertEquals("boom", error.message)
        assertSame(cause, error.throwable)
    }

    // endregion

    /** Gọi một request tới MockEngine trả về [status] và lấy exception Ktor ném ra. */
    private suspend fun exceptionForStatus(status: HttpStatusCode): Throwable {
        val client = HttpClient(MockEngine { respond(content = "", status = status) }) {
            expectSuccess = true
            followRedirects = false
        }
        try {
            return assertFails { client.get("https://test/") }
        } finally {
            client.close()
        }
    }
}

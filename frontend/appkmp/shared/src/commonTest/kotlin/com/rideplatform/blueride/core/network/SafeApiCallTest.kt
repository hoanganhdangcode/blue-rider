package com.rideplatform.blueride.core.network

import com.rideplatform.blueride.core.error.AppError
import com.rideplatform.blueride.core.error.AppResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * Spec cho phương án A (đã chốt):
 * - Một hàm top-level `safeApiCall` trong package `core.network`, trả `AppResult<T?>`.
 * - `"0"` luôn là Success, kể cả khi `data` null → caller tự quyết null nghĩa là gì.
 */
class SafeApiCallTest {

    @Serializable
    data class RideDto(val id: String)

    // region safeApiCall — envelope

    @Test
    fun successCode_withData_returnsSuccess() = runTest {
        val client = clientReturning("""{"code":"0","data":{"id":"r1"},"message":null}""")

        val result = safeApiCall { client.get("rides/r1").body<ApiResponse<RideDto>>() }

        assertEquals(AppResult.Success(RideDto("r1")), result)
    }

    @Test
    fun successCode_withNullData_returnsSuccessNull() = runTest {
        val client = clientReturning("""{"code":"0","data":null}""")

        val result = safeApiCall { client.get("rides/r1").body<ApiResponse<RideDto>>() }

        assertEquals(AppResult.Success(null), result)
    }

    @Test
    fun successCode_withoutDataField_returnsSuccessNull() = runTest {
        // Endpoint kiểu logout/delete: backend chỉ trả {"code":"0"}.
        val client = clientReturning("""{"code":"0"}""")

        val result = safeApiCall { client.get("logout").body<ApiResponse<Unit>>() }

        assertEquals(AppResult.Success(null), result)
    }

    @Test
    fun expireCode_returnsSessionExpired() = runTest {
        val client = clientReturning("""{"code":"9999","message":"Session expired"}""")

        val result = safeApiCall { client.get("rides/r1").body<ApiResponse<RideDto>>() }

        assertEquals(AppResult.Error(AppError.SessionExpired), result)
    }

    @Test
    fun otherCode_returnsBusinessWithCodeAndMessage() = runTest {
        val client = clientReturning("""{"code":"E01","message":"Driver not found"}""")

        val result = safeApiCall { client.get("rides/r1").body<ApiResponse<RideDto>>() }

        assertEquals(AppResult.Error(AppError.Business("E01", "Driver not found")), result)
    }

    // endregion

    // region safeApiCall — exception

    @Test
    fun httpError_returnsServer() = runTest {
        val client = clientReturning("""{"code":"500"}""", HttpStatusCode.InternalServerError)

        val result = safeApiCall { client.get("rides/r1").body<ApiResponse<RideDto>>() }

        assertIs<AppResult.Error>(result)
        val error = assertIs<AppError.Server>(result.error)
        assertEquals(500, error.httpStatus)
    }

    @Test
    fun ioException_returnsNetwork() = runTest {
        val result = safeApiCall<RideDto> { throw IOException("No internet") }

        assertIs<AppResult.Error>(result)
        assertIs<AppError.Network>(result.error)
    }

    @Test
    fun cancellation_isRethrown_notWrappedAsError() = runTest {
        // Nếu bị bắt và bọc thành AppResult.Error, coroutine sẽ không huỷ được.
        assertFailsWith<CancellationException> {
            safeApiCall<RideDto> { throw CancellationException("cancelled") }
        }
    }

    // endregion

    /** Client giống cấu hình HttpClientFactory (expectSuccess + JSON), nhưng dùng MockEngine. */
    private fun clientReturning(
        body: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ) = HttpClient(MockEngine) {
        expectSuccess = true
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        engine {
            addHandler {
                respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
            }
        }
    }
}

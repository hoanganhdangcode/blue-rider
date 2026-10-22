package com.rideplatform.blueride.core.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApiResponseTest {

    @Serializable
    data class TestItem(val id: Int, val name: String)

    // Cùng cấu hình với HttpClientFactory
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun decodesSuccessEnvelopeWithObjectData() {
        val response = json.decodeFromString<ApiResponse<TestItem>>(
            """{"code":"0","data":{"id":1,"name":"Bike"},"message":null}"""
        )

        assertEquals("0", response.code)
        assertEquals(TestItem(1, "Bike"), response.data)
        assertNull(response.message)
    }

    @Test
    fun decodesEnvelopeWithListData() {
        val response = json.decodeFromString<ApiResponse<List<TestItem>>>(
            """{"code":"0","data":[{"id":1,"name":"Bike"},{"id":2,"name":"Car"}]}"""
        )

        assertEquals(listOf(TestItem(1, "Bike"), TestItem(2, "Car")), response.data)
    }

    @Test
    fun decodesEnvelopeWithoutDataAndMessage() {
        val response = json.decodeFromString<ApiResponse<TestItem>>("""{"code":"9999"}""")

        assertEquals("9999", response.code)
        assertNull(response.data)
        assertNull(response.message)
    }

    @Test
    fun decodesUnitDataWhenServerSendsNull() {
        val response = json.decodeFromString<ApiResponse<Unit>>("""{"code":"0","data":null}""")

        assertEquals("0", response.code)
        assertNull(response.data)
    }

    @Test
    fun ignoresUnknownKeys() {
        val response = json.decodeFromString<ApiResponse<TestItem>>(
            """{"code":"1001","message":"Invalid phone","traceId":"abc123"}"""
        )

        assertEquals("1001", response.code)
        assertEquals("Invalid phone", response.message)
    }
}

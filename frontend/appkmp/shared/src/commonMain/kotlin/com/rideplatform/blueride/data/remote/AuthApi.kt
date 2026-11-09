package com.rideplatform.blueride.data.remote

import com.rideplatform.blueride.core.network.ApiResponse
import com.rideplatform.blueride.data.remote.dto.CheckPhoneRequest
import com.rideplatform.blueride.data.remote.dto.CheckPhoneResponseDto
import com.rideplatform.blueride.data.remote.dto.LoginRequest
import com.rideplatform.blueride.data.remote.dto.RegisterRequest
import com.rideplatform.blueride.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

// Đường dẫn endpoint tạm đặt theo quy ước REST thông thường — cần đối chiếu lại
// với core-service khi backend chốt route thật (hiện AuthController chưa tồn tại).
@Single
class AuthApi(private val httpClient: HttpClient) {

    suspend fun checkPhone(phone: String): ApiResponse<CheckPhoneResponseDto> =
        httpClient.post("auth/check-phone") {
            setBody(CheckPhoneRequest(phone))
        }.body()

    suspend fun register(phone: String, otpId: String, otpEncrypt: String): ApiResponse<UserDto> =
        httpClient.post("auth/register") {
            setBody(RegisterRequest(phone, otpId, otpEncrypt))
        }.body()

    suspend fun login(phone: String, pinEncrypt: String): ApiResponse<UserDto> =
        httpClient.post("auth/login") {
            setBody(LoginRequest(phone, pinEncrypt))
        }.body()
}

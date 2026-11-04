package com.rideplatform.blueride.data.remote.dto

import com.rideplatform.blueride.core.network.ApiRequest
import kotlinx.serialization.Serializable

// Giả định tạm thời về field request/response — backend (core-service) đang viết
// song song, RegisterUserUsecase hiện còn rỗng. Cần đối chiếu lại khi backend chốt API.

@Serializable
data class CheckPhoneRequest(
    val phone: String,
) : ApiRequest

@Serializable
data class CheckPhoneResponseDto(
    val status: String, // "NEW" | "EXISTING" — map sang PhoneCheckResult ở tầng repository
    val otpId: String? = null,
)

@Serializable
data class RegisterRequest(
    val phone: String,
    val otpId: String,
    val otpEncrypt: String,
) : ApiRequest

@Serializable
data class LoginRequest(
    val phone: String,
    val pinEncrypt: String,
) : ApiRequest

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val phoneNumber: String,
)

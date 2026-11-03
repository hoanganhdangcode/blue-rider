package com.rideplatform.blueride.domain.repository

interface AuthRepository {
    suspend fun checkPhone(phone: String)
    suspend fun register(phone: String, otpId: String, otpEncrypt: String)
    suspend fun login(phone: String, pinEncrypt: String)
}
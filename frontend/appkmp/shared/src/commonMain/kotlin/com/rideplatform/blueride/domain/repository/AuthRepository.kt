package com.rideplatform.blueride.domain.repository

import com.rideplatform.blueride.core.error.AppResult
import com.rideplatform.blueride.domain.model.PhoneCheckResult
import com.rideplatform.blueride.domain.model.User

interface AuthRepository {
    suspend fun checkPhone(phone: String): AppResult<PhoneCheckResult?>
    suspend fun register(phone: String, otpId: String, otpEncrypt: String): AppResult<User?>
    suspend fun login(phone: String, pinEncrypt: String): AppResult<User?>
}

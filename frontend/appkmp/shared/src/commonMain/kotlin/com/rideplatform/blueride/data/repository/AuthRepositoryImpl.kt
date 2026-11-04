package com.rideplatform.blueride.data.repository

import com.rideplatform.blueride.core.error.AppResult
import com.rideplatform.blueride.core.network.safeApiCall
import com.rideplatform.blueride.data.remote.AuthApi
import com.rideplatform.blueride.data.remote.dto.CheckPhoneResponseDto
import com.rideplatform.blueride.data.remote.dto.UserDto
import com.rideplatform.blueride.domain.model.PhoneCheckResult
import com.rideplatform.blueride.domain.model.User
import com.rideplatform.blueride.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {

    override suspend fun checkPhone(phone: String): AppResult<PhoneCheckResult?> {
        return when (val result = safeApiCall { api.checkPhone(phone) }) {
            is AppResult.Success -> AppResult.Success(result.data?.toDomain())
            is AppResult.Error -> result
        }
    }

    override suspend fun register(phone: String, otpId: String, otpEncrypt: String): AppResult<User?> {
        return when (val result = safeApiCall { api.register(phone, otpId, otpEncrypt) }) {
            is AppResult.Success -> AppResult.Success(result.data?.toDomain())
            is AppResult.Error -> result
        }
    }

    override suspend fun login(phone: String, pinEncrypt: String): AppResult<User?> {
        return when (val result = safeApiCall { api.login(phone, pinEncrypt) }) {
            is AppResult.Success -> AppResult.Success(result.data?.toDomain())
            is AppResult.Error -> result
        }
    }

    private fun CheckPhoneResponseDto.toDomain(): PhoneCheckResult =
        when (status) {
            "NEW" -> PhoneCheckResult.NewUser(otpId = otpId.orEmpty())
            else -> PhoneCheckResult.ExistingUser
        }

    private fun UserDto.toDomain(): User = User(
        id = id,
        name = name,
        avatar = avatar.orEmpty(),
        phoneNumber = phoneNumber,
    )
}

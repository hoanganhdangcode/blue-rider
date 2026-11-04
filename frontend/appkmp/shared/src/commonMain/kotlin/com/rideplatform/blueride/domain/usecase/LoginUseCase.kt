package com.rideplatform.blueride.domain.usecase

import com.rideplatform.blueride.core.error.AppResult
import com.rideplatform.blueride.domain.model.User
import com.rideplatform.blueride.domain.repository.AuthRepository
import org.koin.core.annotation.Factory

@Factory
class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(phone: String, pinEncrypt: String): AppResult<User?> =
        repository.login(phone, pinEncrypt)
}
class ChuyenTienUsecase(private val repository: AuthRepository){
    suspend  fun chuyentiennapas(phone: String, pinEncrypt: String): AppResult<User?> =
        repository.login(phone, pinEncrypt)
    suspend  fun chuyentiencitad(phone: String, pinEncrypt: String): AppResult<User?> =
        repository.login(phone, pinEncrypt)
    suspend  fun chuyentienqr(phone: String, pinEncrypt: String): AppResult<User?> =
        repository.login(phone, pinEncrypt)
}
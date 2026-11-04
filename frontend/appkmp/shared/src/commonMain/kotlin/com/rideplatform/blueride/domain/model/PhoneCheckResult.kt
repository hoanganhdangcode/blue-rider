package com.rideplatform.blueride.domain.model

sealed interface PhoneCheckResult {
    data class NewUser(val otpId: String) : PhoneCheckResult
    data object ExistingUser : PhoneCheckResult
}

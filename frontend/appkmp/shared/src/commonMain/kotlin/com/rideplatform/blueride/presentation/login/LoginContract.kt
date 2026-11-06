package com.rideplatform.blueride.presentation.login

import com.rideplatform.blueride.core.error.AppError
import com.rideplatform.blueride.presentation.base.UIEffect
import com.rideplatform.blueride.presentation.base.UIIntent
import com.rideplatform.blueride.presentation.base.UIState

data class LoginUiState(
    val phone: String = "",
    val pin: String = "",
    val isLoading: Boolean = false,
    val error: AppError? = null,
) : UIState

sealed interface LoginUiEvent : UIIntent {
    data class PhoneChanged(val value: String) : LoginUiEvent
    data class PinChanged(val value: String) : LoginUiEvent
    data object Submit : LoginUiEvent
}

sealed interface LoginUiEffect : UIEffect {
    data object NavigateToUpdateProfile : LoginUiEffect
}

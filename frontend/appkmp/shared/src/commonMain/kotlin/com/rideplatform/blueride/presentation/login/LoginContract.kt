package com.rideplatform.blueride.presentation.login

import com.rideplatform.blueride.core.error.AppError
import com.rideplatform.blueride.presentation.base.UiEffect
import com.rideplatform.blueride.presentation.base.UiEvent
import com.rideplatform.blueride.presentation.base.UiState

// State là 1 data class phẳng (không sealed Loading/Success/Error như mẫu) vì đây là
// màn hình form: phải giữ lại giá trị phone/pin người dùng đã nhập xuyên suốt lúc
// loading/error, sealed state kiểu list-fetch sẽ làm mất giá trị form mỗi lần đổi state.
data class LoginUiState(
    val phone: String = "",
    val pin: String = "",
    val isLoading: Boolean = false,
    val error: AppError? = null,
) : UiState

sealed interface LoginUiEvent : UiEvent {
    data class PhoneChanged(val value: String) : LoginUiEvent
    data class PinChanged(val value: String) : LoginUiEvent
    data object Submit : LoginUiEvent
}

sealed interface LoginUiEffect : UiEffect {
    data object NavigateToUpdateProfile : LoginUiEffect
}

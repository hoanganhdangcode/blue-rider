package com.rideplatform.blueride.presentation.login

import androidx.lifecycle.viewModelScope
import com.rideplatform.blueride.core.error.AppResult
import com.rideplatform.blueride.domain.usecase.LoginUseCase
import com.rideplatform.blueride.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginUiState, LoginUiEvent, LoginUiEffect>(LoginUiState()) {

    override fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.PhoneChanged -> updateState { it.copy(phone = event.value, error = null) }
            is LoginUiEvent.PinChanged -> updateState { it.copy(pin = event.value, error = null) }
            is LoginUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        updateState { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = loginUseCase(currentState.phone, currentState.pin)) {
                is AppResult.Success -> {
                    updateState { it.copy(isLoading = false) }
                    emitEffect(LoginUiEffect.NavigateToUpdateProfile)
                }
                is AppResult.Error -> {
                    updateState { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }
}

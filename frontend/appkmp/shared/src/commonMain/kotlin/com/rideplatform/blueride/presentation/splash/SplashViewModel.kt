package com.rideplatform.blueride.presentation.splash

import androidx.lifecycle.viewModelScope
import com.rideplatform.blueride.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel : BaseViewModel<SplashUiState, SplashUiEvent, SplashUiEffect>(SplashUiState) {

    init {
        viewModelScope.launch {
            // TODO: thay delay cố định bằng điều kiện init thật (vd AppConfig load xong) khi có UseCase
            delay(1500)
            emitEffect(SplashUiEffect.NavigateToMain)
        }
    }

    override fun handleEvent(event: SplashUiEvent) {
    }
}

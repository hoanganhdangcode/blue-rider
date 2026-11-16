package com.rideplatform.blueride.presentation.splash

import com.rideplatform.blueride.presentation.base.UIEffect
import com.rideplatform.blueride.presentation.base.UIIntent
import com.rideplatform.blueride.presentation.base.UIState

data object SplashUiState : UIState

sealed interface SplashUiEvent : UIIntent

sealed interface SplashUiEffect : UIEffect {
    data object NavigateToMain : SplashUiEffect
}

package com.rideplatform.blueride.presentation

import androidx.compose.runtime.Composable
import com.rideplatform.blueride.presentation.login.LoginScreen
import com.rideplatform.blueride.presentation.login.LoginViewModel
import com.rideplatform.blueride.presentation.splash.SplashScreen
import com.rideplatform.blueride.presentation.splash.SplashViewModel
import com.rideplatform.blueride.presentation.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel
@Composable
fun App() {
    AppTheme {
        val viewModel = koinViewModel<SplashViewModel>()
        SplashScreen(
            viewModel = viewModel,
            onNavigateToMain = { /* TODO: điều hướng sang Home khi có RootComponent */ },
        )
    }
}

package com.rideplatform.blueride.presentation

import androidx.compose.runtime.Composable
import com.rideplatform.blueride.presentation.login.LoginScreen
import com.rideplatform.blueride.presentation.login.LoginViewModel
import com.rideplatform.blueride.presentation.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

// TODO: thay bằng điều hướng Decompose (RootComponent) khi hoàn thiện — hiện tạm gọi thẳng LoginScreen.
@Composable
fun App() {
    AppTheme {
        val viewModel = koinViewModel<LoginViewModel>()
        LoginScreen(
            viewModel = viewModel,
            onNavigateToHome = { /* TODO: điều hướng sang Home khi có RootComponent */ },
        )
    }
}

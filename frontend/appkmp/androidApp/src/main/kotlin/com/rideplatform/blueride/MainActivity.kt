package com.rideplatform.blueride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rideplatform.blueride.presentation.App
import com.rideplatform.blueride.presentation.splash.SplashScreen
import com.rideplatform.blueride.presentation.splash.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {
                val viewModel = koinViewModel<SplashViewModel>()
                SplashScreen(
                    viewModel = viewModel,
                    onNavigateToMain = { showSplash = false },
                )
            } else {
                App()
            }
        }
    }
}

@Preview
@Composable
private fun AppAndroidPreview() {
    App()
}

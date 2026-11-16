package com.rideplatform.blueride.presentation.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.koin.core.annotation.Single

@Single
class ThemeController {
    var themeMode: ThemeMode by mutableStateOf(ThemeMode.Light)
        private set

    fun selectThemeMode(value: ThemeMode) {
        themeMode = value
    }
}

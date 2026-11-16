package com.rideplatform.blueride.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import com.rideplatform.blueride.presentation.theme.brand.BlueRideColors
import org.koin.compose.koinInject

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val isPreview = LocalInspectionMode.current
    val themeMode: ThemeMode = if (isPreview) {
        ThemeMode.Light
    } else {
        val themeController = koinInject<ThemeController>()
        themeController.themeMode
    }

    val isDark = themeMode == ThemeMode.Dark

    val (colorScheme, extendedColors) = blueRideScheme(isDark)

    if (!isPreview) {
        SystemBarsEffect(isDark = isDark)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(),
        shapes = AppShapes,
    ) {
        CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
            content()
        }
    }
}

private fun blueRideScheme(isDark: Boolean): Pair<ColorScheme, ExtendedColors> =
    if (isDark) {
        BlueRideColors.darkScheme to BlueRideColors.extendedDark
    } else {
        BlueRideColors.lightScheme to BlueRideColors.extendedLight
    }

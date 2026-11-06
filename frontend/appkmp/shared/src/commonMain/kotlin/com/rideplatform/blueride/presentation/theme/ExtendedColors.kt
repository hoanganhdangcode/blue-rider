package com.rideplatform.blueride.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Semantic token ngoài Material3 ColorScheme (success/warning/info, divider, text phụ...).
data class ExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val info: Color,
    val onInfo: Color,
    val divider: Color,
    val textTertiary: Color,
    val inputBackground: Color,
)

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColors> {
    error("Chưa có ExtendedColors — bọc nội dung bằng AppTheme { }")
}

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    get() = LocalExtendedColors.current

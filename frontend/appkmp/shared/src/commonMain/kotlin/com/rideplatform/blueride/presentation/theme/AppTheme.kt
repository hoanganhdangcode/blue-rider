package com.rideplatform.blueride.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import com.rideplatform.blueride.presentation.theme.brand.BlueRideColors
import com.rideplatform.blueride.presentation.theme.brand.EcoRideColors
import com.rideplatform.blueride.presentation.theme.brand.VietRideColors
import org.koin.compose.koinInject

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val isPreview = LocalInspectionMode.current
    val brand: Brand
    val themeMode: ThemeMode
    if (isPreview) {
        brand = Brand.BlueRide
        themeMode = ThemeMode.Light
    } else {
        val themeController = koinInject<ThemeController>()
        brand = themeController.brand
        themeMode = themeController.themeMode
    }

    val isDark = themeMode == ThemeMode.Dark

    val (colorScheme, extendedColors) = brandSchemes(brand, isDark)

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

private fun brandSchemes(brand: Brand, isDark: Boolean): Pair<ColorScheme, ExtendedColors> =
    when (brand) {
        Brand.BlueRide -> if (isDark) {
            BlueRideColors.darkScheme to BlueRideColors.extendedDark
        } else {
            BlueRideColors.lightScheme to BlueRideColors.extendedLight
        }

        Brand.VietRide -> if (isDark) {
            VietRideColors.darkScheme to VietRideColors.extendedDark
        } else {
            VietRideColors.lightScheme to VietRideColors.extendedLight
        }

        Brand.EcoRide -> if (isDark) {
            EcoRideColors.darkScheme to EcoRideColors.extendedDark
        } else {
            EcoRideColors.lightScheme to EcoRideColors.extendedLight
        }
    }

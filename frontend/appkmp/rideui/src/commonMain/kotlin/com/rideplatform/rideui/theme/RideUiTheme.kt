package com.rideplatform.rideui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RideColorScheme = lightColorScheme(
    primary = RidePrimary,
    primaryContainer = RidePrimaryContainer,
    secondary = RideSecondary,
    background = RideBackground,
    surface = RideSurface,
    onPrimary = RideOnPrimary,
    onBackground = RideOnBackground,
    error = RideError,
)

@Composable
fun RideUiTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = RideColorScheme,
        typography = RideTypography,
        content = content,
    )
}

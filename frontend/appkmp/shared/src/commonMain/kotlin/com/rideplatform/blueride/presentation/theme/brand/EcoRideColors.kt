package com.rideplatform.blueride.presentation.theme.brand

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.rideplatform.blueride.presentation.theme.ExtendedColors

internal object EcoRideColors {
    val lightScheme = lightColorScheme(
        primary = Color(0xFF006A64),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF00B2A9),
        onPrimaryContainer = Color(0xFF003E3A),
        inversePrimary = Color(0xFF50DAD1),
        secondary = Color(0xFF4E6267),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFCEE3EA),
        onSecondaryContainer = Color(0xFF52666C),
        tertiary = Color(0xFF006A66),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFF3EAFA9),
        onTertiaryContainer = Color(0xFF003D3B),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        background = Color(0xFFF4FBF9),
        onBackground = Color(0xFF161D1C),
        surface = Color(0xFFF4FBF9),
        onSurface = Color(0xFF161D1C),
        surfaceVariant = Color(0xFFDDE4E2),
        onSurfaceVariant = Color(0xFF3C4948),
        surfaceTint = Color(0xFF006A64),
        inverseSurface = Color(0xFF2B3231),
        inverseOnSurface = Color(0xFFECF2F0),
        outline = Color(0xFF6C7A78),
        outlineVariant = Color(0xFFBBC9C7),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFEFF5F3),
        surfaceContainer = Color(0xFFE9EFED),
        surfaceContainerHigh = Color(0xFFE3E9E8),
        surfaceContainerHighest = Color(0xFFDDE4E2),
    )

    val darkScheme = darkColorScheme(
        primary = Color(0xFF50DAD1),
        onPrimary = Color(0xFF00201E),
        primaryContainer = Color(0xFF00504B),
        onPrimaryContainer = Color(0xFF72F7ED),
        inversePrimary = Color(0xFF006A64),
        secondary = Color(0xFFB5CAD1),
        onSecondary = Color(0xFF1F333A),
        secondaryContainer = Color(0xFF374B51),
        onSecondaryContainer = Color(0xFFCEE3EA),
        tertiary = Color(0xFF6CD8D1),
        onTertiary = Color(0xFF00201E),
        tertiaryContainer = Color(0xFF00504C),
        onTertiaryContainer = Color(0xFF8AF4ED),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF0C1413),
        onBackground = Color(0xFFDDE4E2),
        surface = Color(0xFF0C1413),
        onSurface = Color(0xFFDDE4E2),
        surfaceVariant = Color(0xFF3C4948),
        onSurfaceVariant = Color(0xFFBBC9C7),
        surfaceTint = Color(0xFF50DAD1),
        inverseSurface = Color(0xFFDDE4E2),
        inverseOnSurface = Color(0xFF2B3231),
        outline = Color(0xFF869390),
        outlineVariant = Color(0xFF3C4948),
        surfaceContainerLowest = Color(0xFF070E0D),
        surfaceContainerLow = Color(0xFF161D1C),
        surfaceContainer = Color(0xFF1A211F),
        surfaceContainerHigh = Color(0xFF242B2A),
        surfaceContainerHighest = Color(0xFF2F3635),
    )

    val extendedLight = ExtendedColors(
        success = Color(0xFF16A34A),
        onSuccess = Color(0xFFFFFFFF),
        warning = Color(0xFFF59E0B),
        onWarning = Color(0xFF3A2300),
        info = Color(0xFF006A64),
        onInfo = Color(0xFFFFFFFF),
        divider = Color(0xFFBBC9C7),
        textTertiary = Color(0xFF6C7A78),
        inputBackground = Color(0xFFEFF5F3),
    )

    val extendedDark = ExtendedColors(
        success = Color(0xFF4ADE80),
        onSuccess = Color(0xFF07310E),
        warning = Color(0xFFFBBF24),
        onWarning = Color(0xFF3A2300),
        info = Color(0xFF6CD8D1),
        onInfo = Color(0xFF00201E),
        divider = Color(0xFF3C4948),
        textTertiary = Color(0xFF869390),
        inputBackground = Color(0xFF161D1C),
    )
}

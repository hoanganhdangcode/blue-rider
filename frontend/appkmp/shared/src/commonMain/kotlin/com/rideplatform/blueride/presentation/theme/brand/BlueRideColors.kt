package com.rideplatform.blueride.presentation.theme.brand

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.rideplatform.blueride.presentation.theme.ExtendedColors

// Nguồn: Stitch "BlueRide Vietnam" (thiết kế hiện có của app) — Deep Vietnamese Blue + Red.
public object BlueRideColors {
    val lightScheme = lightColorScheme(
        primary = Color(0xFF002576),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF0038A8),
        onPrimaryContainer = Color(0xFF96ADFF),
        inversePrimary = Color(0xFFB6C4FF),
        secondary = Color(0xFFBB0308),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE02A21),
        onSecondaryContainer = Color(0xFFFFFBFF),
        tertiary = Color(0xFF292E31),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFF3F4447),
        onTertiaryContainer = Color(0xFFACB1B5),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        background = Color(0xFFF9F9FC),
        onBackground = Color(0xFF1A1C1E),
        surface = Color(0xFFF9F9FC),
        onSurface = Color(0xFF1A1C1E),
        surfaceVariant = Color(0xFFE2E2E5),
        onSurfaceVariant = Color(0xFF444653),
        surfaceTint = Color(0xFF3056C4),
        inverseSurface = Color(0xFF2F3133),
        inverseOnSurface = Color(0xFFF0F0F3),
        outline = Color(0xFF747685),
        outlineVariant = Color(0xFFC4C5D5),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF3F3F6),
        surfaceContainer = Color(0xFFEEEEF0),
        surfaceContainerHigh = Color(0xFFE8E8EA),
        surfaceContainerHighest = Color(0xFFE2E2E5),
    )

    val darkScheme = darkColorScheme(
        primary = Color(0xFFB6C4FF),
        onPrimary = Color(0xFF00164F),
        primaryContainer = Color(0xFF093CAB),
        onPrimaryContainer = Color(0xFFDCE1FF),
        inversePrimary = Color(0xFF3056C4),
        secondary = Color(0xFFFFB4A9),
        onSecondary = Color(0xFF410001),
        secondaryContainer = Color(0xFF930004),
        onSecondaryContainer = Color(0xFFFFDAD5),
        tertiary = Color(0xFFC3C7CB),
        onTertiary = Color(0xFF171C1F),
        tertiaryContainer = Color(0xFF43474B),
        onTertiaryContainer = Color(0xFFDFE3E7),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF121318),
        onBackground = Color(0xFFE2E2E5),
        surface = Color(0xFF121318),
        onSurface = Color(0xFFE2E2E5),
        surfaceVariant = Color(0xFF444653),
        onSurfaceVariant = Color(0xFFC4C5D5),
        surfaceTint = Color(0xFFB6C4FF),
        inverseSurface = Color(0xFFE2E2E5),
        inverseOnSurface = Color(0xFF2F3133),
        outline = Color(0xFF8E9099),
        outlineVariant = Color(0xFF444653),
        surfaceContainerLowest = Color(0xFF0C0D11),
        surfaceContainerLow = Color(0xFF1A1C1E),
        surfaceContainer = Color(0xFF1E2024),
        surfaceContainerHigh = Color(0xFF282A2F),
        surfaceContainerHighest = Color(0xFF33353A),
    )

    val extendedLight = ExtendedColors(
        success = Color(0xFF16A34A),
        onSuccess = Color(0xFFFFFFFF),
        warning = Color(0xFFF59E0B),
        onWarning = Color(0xFF3A2300),
        info = Color(0xFF0076D1),
        onInfo = Color(0xFFFFFFFF),
        divider = Color(0xFFC4C5D5),
        textTertiary = Color(0xFF747685),
        inputBackground = Color(0xFFF3F3F6),
    )

    val extendedDark = ExtendedColors(
        success = Color(0xFF4ADE80),
        onSuccess = Color(0xFF07310E),
        warning = Color(0xFFFBBF24),
        onWarning = Color(0xFF3A2300),
        info = Color(0xFFA3C9FF),
        onInfo = Color(0xFF001C39),
        divider = Color(0xFF444653),
        textTertiary = Color(0xFF8E9099),
        inputBackground = Color(0xFF1E2024),
    )
}

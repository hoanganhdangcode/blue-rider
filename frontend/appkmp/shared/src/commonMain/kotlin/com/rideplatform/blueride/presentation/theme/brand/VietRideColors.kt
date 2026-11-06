package com.rideplatform.blueride.presentation.theme.brand

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.rideplatform.blueride.presentation.theme.ExtendedColors

// Nguồn: Stitch "Vivu: Ride-Hailing Platform" — "VietRide Design System".
// Dùng bộ màu brand nổi bật (override) làm primary/secondary/tertiary thay vì bản "đậm"
// để tránh trùng primary với BlueRide: Deep Blue #0038A8, Sky Blue #0091FF, Heritage Red #DA251D.
internal object VietRideColors {
    val lightScheme = lightColorScheme(
        primary = Color(0xFF0038A8),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF002576),
        onPrimaryContainer = Color(0xFFDCE1FF),
        inversePrimary = Color(0xFFB6C4FF),
        secondary = Color(0xFF0091FF),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF0076D1),
        onSecondaryContainer = Color(0xFFFDFCFF),
        tertiary = Color(0xFFDA251D),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFF8D0004),
        onTertiaryContainer = Color(0xFFFF9284),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        background = Color(0xFFF7F9FB),
        onBackground = Color(0xFF191C1E),
        surface = Color(0xFFF7F9FB),
        onSurface = Color(0xFF191C1E),
        surfaceVariant = Color(0xFFE0E3E5),
        onSurfaceVariant = Color(0xFF444653),
        surfaceTint = Color(0xFF0038A8),
        inverseSurface = Color(0xFF2D3133),
        inverseOnSurface = Color(0xFFEFF1F3),
        outline = Color(0xFF747685),
        outlineVariant = Color(0xFFC4C5D5),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF2F4F6),
        surfaceContainer = Color(0xFFECEEF0),
        surfaceContainerHigh = Color(0xFFE6E8EA),
        surfaceContainerHighest = Color(0xFFE0E3E5),
    )

    val darkScheme = darkColorScheme(
        primary = Color(0xFFB6C4FF),
        onPrimary = Color(0xFF00164F),
        primaryContainer = Color(0xFF093CAB),
        onPrimaryContainer = Color(0xFFDCE1FF),
        inversePrimary = Color(0xFF0038A8),
        secondary = Color(0xFFA3C9FF),
        onSecondary = Color(0xFF001C39),
        secondaryContainer = Color(0xFF004883),
        onSecondaryContainer = Color(0xFFD3E3FF),
        tertiary = Color(0xFFFFB4A9),
        onTertiary = Color(0xFF690005),
        tertiaryContainer = Color(0xFF8D0004),
        onTertiaryContainer = Color(0xFFFFDAD6),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF101317),
        onBackground = Color(0xFFE0E3E5),
        surface = Color(0xFF101317),
        onSurface = Color(0xFFE0E3E5),
        surfaceVariant = Color(0xFF444653),
        onSurfaceVariant = Color(0xFFC4C5D5),
        surfaceTint = Color(0xFFB6C4FF),
        inverseSurface = Color(0xFFE0E3E5),
        inverseOnSurface = Color(0xFF2D3133),
        outline = Color(0xFF8E9099),
        outlineVariant = Color(0xFF444653),
        surfaceContainerLowest = Color(0xFF0B0E12),
        surfaceContainerLow = Color(0xFF191C1E),
        surfaceContainer = Color(0xFF1D2023),
        surfaceContainerHigh = Color(0xFF27292D),
        surfaceContainerHighest = Color(0xFF323539),
    )

    val extendedLight = ExtendedColors(
        success = Color(0xFF16A34A),
        onSuccess = Color(0xFFFFFFFF),
        warning = Color(0xFFF59E0B),
        onWarning = Color(0xFF3A2300),
        info = Color(0xFF0091FF),
        onInfo = Color(0xFFFFFFFF),
        divider = Color(0xFFC4C5D5),
        textTertiary = Color(0xFF747685),
        inputBackground = Color(0xFFF2F4F6),
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
        inputBackground = Color(0xFF191C1E),
    )
}

package com.rideplatform.blueride.presentation.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun SystemBarsEffect(isDark: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return

    val window = (view.context as Activity).window
    SideEffect {
        // enableEdgeToEdge() ở MainActivity đã bật edge-to-edge; ở đây chỉ đồng bộ icon
        // sáng/tối theo Theme hiện tại và giữ system bars trong suốt để nội dung tràn viền.
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = !isDark
        controller.isAppearanceLightNavigationBars = !isDark
    }
}

package com.rideplatform.blueride.presentation.theme

import androidx.compose.runtime.Composable

// Đồng bộ màu/kiểu icon Status Bar & Navigation Bar với Theme hiện tại.
// Android: xử lý thật (WindowCompat insets controller) trong SystemBars.android.kt.
// iOS: no-op ở bản này — nằm ngoài phạm vi yêu cầu hiện tại.
@Composable
expect fun SystemBarsEffect(isDark: Boolean)

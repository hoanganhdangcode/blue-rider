package com.rideplatform.blueride.presentation.theme

import androidx.compose.runtime.Composable

// Chưa xử lý status bar style cho iOS ở bản này — nằm ngoài phạm vi yêu cầu hiện tại
// ("phần Android-specific system bars xử lý trong androidMain/androidApp").
@Composable
actual fun SystemBarsEffect(isDark: Boolean) {
}

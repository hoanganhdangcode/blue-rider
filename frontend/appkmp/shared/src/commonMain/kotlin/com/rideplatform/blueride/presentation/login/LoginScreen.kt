package com.rideplatform.blueride.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import com.rideplatform.blueride.core.error.AppError
import com.rideplatform.blueride.presentation.component.PrimaryButton
import com.rideplatform.blueride.presentation.component.RideTextField
import com.rideplatform.blueride.presentation.theme.AppSpacing
import com.rideplatform.blueride.presentation.theme.AppTheme
import com.rideplatform.blueride.presentation.theme.Brand
import com.rideplatform.blueride.presentation.theme.ThemeController
import com.rideplatform.blueride.presentation.theme.ThemeMode
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeController = koinInject<ThemeController>()

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                LoginUiEffect.NavigateToUpdateProfile -> onNavigateToHome()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Demo Multi Theme Design System: đổi Brand + Light/Dark/System tại runtime.
        ThemeSwitcherRow(
            themeController = themeController,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.screenMargin, vertical = AppSpacing.sm),
        )

        LoginContent(
            state = uiState,
            onEvent = viewModel::handleEvent,
        )
    }
}

@Composable
private fun ThemeSwitcherRow(
    themeController: ThemeController,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
    ) {
        Brand.entries.forEach { brand ->
            FilterChip(
                selected = themeController.brand == brand,
                onClick = { themeController.selectBrand(brand) },
                label = { Text(brand.displayName, style = MaterialTheme.typography.labelMedium) },
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {
                val next = when (themeController.themeMode) {
                    ThemeMode.Light -> ThemeMode.Dark
                    ThemeMode.Dark -> ThemeMode.Light
                }
                themeController.selectThemeMode(next)
            },
        ) {
            Text(
                text = when (themeController.themeMode) {
                    ThemeMode.Light -> "☀"
                    ThemeMode.Dark -> "🌙"
                },
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

// Tách riêng phần UI thuần (không phụ thuộc LoginViewModel/Koin) để @Preview mock được
// mà không cần khởi tạo DI thật.
@Composable
private fun LoginContent(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.screenMargin)
            .padding(top = AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Chào mừng bạn!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(Modifier.height(AppSpacing.sm))

        Text(
            text = "Vui lòng đăng nhập để tiếp tục.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(AppSpacing.lg))

        RideTextField(
            value = state.phone,
            onValueChange = { onEvent(LoginUiEvent.PhoneChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Số điện thoại",
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
        )

        Spacer(Modifier.height(AppSpacing.md))

        RideTextField(
            value = state.pin,
            onValueChange = { onEvent(LoginUiEvent.PinChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Mã PIN",
            keyboardType = KeyboardType.NumberPassword,
            visualTransformation = PasswordVisualTransformation(),
            imeAction = ImeAction.Done,
            onImeAction = { onEvent(LoginUiEvent.Submit) },
        )

        state.error?.let { error ->
            Spacer(Modifier.height(AppSpacing.sm))
            Text(
                text = error.toDisplayMessage(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(Modifier.height(AppSpacing.lg))

        PrimaryButton(
            text = "Đăng nhập",
            onClick = { onEvent(LoginUiEvent.Submit) },
            enabled = state.phone.isNotBlank() && state.pin.isNotBlank(),
            loading = state.isLoading,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    AppTheme {
        LoginContent(state = LoginUiState(), onEvent = {})
    }
}

@Preview(showBackground = true, name = "Đã nhập + loading")
@Composable
private fun LoginContentLoadingPreview() {
    AppTheme {
        LoginContent(
            state = LoginUiState(phone = "0901234567", pin = "••••", isLoading = true),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Lỗi")
@Composable
private fun LoginContentErrorPreview() {
    AppTheme {
        LoginContent(
            state = LoginUiState(
                phone = "0901234567",
                pin = "0000",
                error = AppError.Business(code = "INVALID_PIN", message = "Số điện thoại hoặc mã PIN không đúng."),
            ),
            onEvent = {},
        )
    }
}

private fun AppError.toDisplayMessage(): String = when (this) {
    is AppError.Network -> message ?: "Không có kết nối mạng. Vui lòng thử lại."
    is AppError.Client -> message ?: "Yêu cầu không hợp lệ."
    is AppError.Server -> message ?: "Hệ thống đang gặp sự cố. Vui lòng thử lại sau."
    is AppError.Business -> message ?: "Số điện thoại hoặc mã PIN không đúng."
    AppError.SessionExpired -> "Phiên đăng nhập đã hết hạn."
    is AppError.Unknown -> message ?: "Đã xảy ra lỗi không xác định."
}

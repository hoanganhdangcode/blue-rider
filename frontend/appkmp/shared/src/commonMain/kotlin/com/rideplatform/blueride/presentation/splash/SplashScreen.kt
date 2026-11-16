package com.rideplatform.blueride.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import blueride.shared.generated.resources.Res
import blueride.shared.generated.resources.splash_layer_background
import com.rideplatform.blueride.presentation.theme.AppTheme
import com.rideplatform.blueride.presentation.theme.brand.BlueRideColors
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToMain: () -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SplashUiEffect.NavigateToMain -> onNavigateToMain()
            }
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BlueRideColors.lightScheme.surface),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(Res.drawable.splash_layer_background),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .scale(1.5f)
                            .rotate(24f),
                        contentScale = ContentScale.FillWidth,
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                    ) {
                        Text(
                            text = "Blue Ride",
                            color = BlueRideColors.lightScheme.primary,
                            style = MaterialTheme.typography.displayLarge,
                        )
                        Text(
                            text = "Nhanh chóng, Tiện lợi",
                            color = BlueRideColors.lightScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    LinearProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 48.dp, vertical = 32.dp),
                        color = BlueRideColors.lightScheme.primary,
                        trackColor = BlueRideColors.lightScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashContentPreview() {
    SplashContent()
}

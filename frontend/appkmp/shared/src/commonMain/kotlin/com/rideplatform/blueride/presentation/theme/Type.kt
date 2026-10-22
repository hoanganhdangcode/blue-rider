package com.rideplatform.blueride.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import blueride.shared.generated.resources.Res
import blueride.shared.generated.resources.inter_bold
import blueride.shared.generated.resources.inter_regular
import blueride.shared.generated.resources.inter_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun appTypography(): Typography {
    val regular = Font(Res.font.inter_regular, FontWeight.W400)
    val semibold = Font(Res.font.inter_semibold, FontWeight.W600)
    val bold = Font(Res.font.inter_bold, FontWeight.W700)
    return remember(regular, semibold, bold) {
        val family = FontFamily(regular, semibold, bold)
        Typography(
            displayLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W700,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.02).em,
            ),
            displayMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W600,
                fontSize = 28.sp,
                lineHeight = 36.sp,
            ),
            displaySmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
            headlineLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W700,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.02).em,
            ),
            headlineMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
            headlineSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
            titleLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W600,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            ),
            titleMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W600,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
            titleSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W600,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
            bodySmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
            labelLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
            labelMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.01.em,
            ),
            labelSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.W500,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.01.em,
            ),
        )
    }
}

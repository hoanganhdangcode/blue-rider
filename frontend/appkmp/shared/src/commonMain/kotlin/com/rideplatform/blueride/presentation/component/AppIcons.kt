package com.rideplatform.blueride.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rideplatform.blueride.presentation.theme.AppSpacing
import com.rideplatform.blueride.presentation.theme.AppTheme

object AppIcons {
        val Home: ImageVector =
            appIcon("Home") {
                moveTo(10f, 20f)
                verticalLineTo(14f)
                horizontalLineTo(14f)
                verticalLineTo(20f)
                horizontalLineTo(19f)
                verticalLineTo(12f)
                horizontalLineTo(22f)
                lineTo(12f, 3f)
                lineTo(2f, 12f)
                horizontalLineTo(5f)
                verticalLineTo(20f)
                close()
            }
        val History: ImageVector =
            appIcon("History") {
                moveTo(13f, 3f)
                arcTo(9f, 9f, 0f, false, false, 4f, 12f)
                horizontalLineTo(1f)
                lineTo(4.89f, 15.89f)
                lineTo(4.96f, 16.03f)
                lineTo(9f, 12f)
                horizontalLineTo(6f)
                curveTo(6f, 8.13f, 9.13f, 5f, 13f, 5f)
                reflectiveCurveTo(20f, 8.13f, 20f, 12f)
                reflectiveCurveTo(16.87f, 19f, 13f, 19f)
                curveTo(11.07f, 19f, 9.32f, 18.21f, 8.06f, 16.94f)
                lineTo(6.64f, 18.36f)
                arcTo(8.954f, 8.954f, 0f, false, false, 13f, 21f)
                arcTo(9f, 9f, 0f, false, false, 13f, 3f)
                close()
                moveTo(12f, 8f)
                verticalLineTo(13f)
                lineTo(16.28f, 15.54f)
                lineTo(17f, 14.33f)
                lineTo(13.5f, 12.25f)
                verticalLineTo(8f)
                horizontalLineTo(12f)
                close()
            }
        val Wallet: ImageVector =
            appIcon("Wallet") {
                moveTo(21f, 18f)
                verticalLineTo(19f)
                curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f)
                horizontalLineTo(5f)
                arcTo(2f, 2f, 0f, false, true, 3f, 19f)
                verticalLineTo(5f)
                arcTo(2f, 2f, 0f, false, true, 5f, 3f)
                horizontalLineTo(19f)
                curveTo(20.1f, 3f, 21f, 3.9f, 21f, 5f)
                verticalLineTo(6f)
                horizontalLineTo(12f)
                arcTo(2f, 2f, 0f, false, false, 10f, 8f)
                verticalLineTo(16f)
                arcTo(2f, 2f, 0f, false, false, 12f, 18f)
                horizontalLineTo(21f)
                close()
                moveTo(12f, 16f)
                horizontalLineTo(22f)
                verticalLineTo(8f)
                horizontalLineTo(12f)
                verticalLineTo(16f)
                close()
                moveTo(16f, 13.5f)
                curveTo(15.17f, 13.5f, 14.5f, 12.83f, 14.5f, 12f)
                reflectiveCurveTo(15.17f, 10.5f, 16f, 10.5f)
                reflectiveCurveTo(17.5f, 11.17f, 17.5f, 12f)
                reflectiveCurveTo(16.83f, 13.5f, 16f, 13.5f)
                close()
            }
        val Offer: ImageVector =
            appIcon("Offer") {
                moveTo(21.41f, 11.58f)
                lineTo(12.41f, 2.58f)
                curveTo(12.05f, 2.22f, 11.55f, 2f, 11f, 2f)
                horizontalLineTo(4f)
                curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f)
                verticalLineTo(11f)
                curveTo(2f, 11.55f, 2.22f, 12.05f, 2.59f, 12.42f)
                lineTo(11.59f, 21.42f)
                curveTo(11.95f, 21.78f, 12.45f, 22f, 13f, 22f)
                curveTo(13.55f, 22f, 14.05f, 21.78f, 14.41f, 21.41f)
                lineTo(21.41f, 14.41f)
                curveTo(21.78f, 14.05f, 22f, 13.55f, 22f, 13f)
                curveTo(22f, 12.45f, 21.77f, 11.94f, 21.41f, 11.58f)
                close()
                moveTo(5.5f, 7f)
                curveTo(4.67f, 7f, 4f, 6.33f, 4f, 5.5f)
                reflectiveCurveTo(4.67f, 4f, 5.5f, 4f)
                reflectiveCurveTo(7f, 4.67f, 7f, 5.5f)
                reflectiveCurveTo(6.33f, 7f, 5.5f, 7f)
                close()
            }
        val Person: ImageVector =
            appIcon("Person") {
                moveTo(12f, 12f)
                curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f)
                reflectiveCurveTo(14.21f, 4f, 12f, 4f)
                reflectiveCurveTo(8f, 5.79f, 8f, 8f)
                reflectiveCurveTo(9.79f, 12f, 12f, 12f)
                close()
                moveTo(12f, 14f)
                curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f)
                verticalLineTo(20f)
                horizontalLineTo(20f)
                verticalLineTo(18f)
                curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f)
                close()
            }
        val Search: ImageVector =
            appIcon("Search") {
                moveTo(15.5f, 14f)
                horizontalLineTo(14.71f)
                lineTo(14.43f, 13.73f)
                arcTo(6.471f, 6.471f, 0f, false, false, 16f, 9.5f)
                arcTo(6.5f, 6.5f, 0f, true, false, 9.5f, 16f)
                curveTo(11.11f, 16f, 12.59f, 15.41f, 13.73f, 14.43f)
                lineTo(14f, 14.71f)
                verticalLineTo(15.5f)
                lineTo(19f, 20.49f)
                lineTo(20.49f, 19f)
                lineTo(15.5f, 14f)
                close()
                moveTo(9.5f, 14f)
                curveTo(7.01f, 14f, 5f, 11.99f, 5f, 9.5f)
                reflectiveCurveTo(7.01f, 5f, 9.5f, 5f)
                reflectiveCurveTo(14f, 7.01f, 14f, 9.5f)
                reflectiveCurveTo(11.99f, 14f, 9.5f, 14f)
                close()
            }
        val Star: ImageVector =
            appIcon("Star") {
                moveTo(12f, 17.27f)
                lineTo(18.18f, 21f)
                lineTo(16.54f, 13.97f)
                lineTo(22f, 9.24f)
                lineTo(14.81f, 8.63f)
                lineTo(12f, 2f)
                lineTo(9.19f, 8.63f)
                lineTo(2f, 9.24f)
                lineTo(7.46f, 13.97f)
                lineTo(5.82f, 21f)
                close()
            }
        val StarHalf: ImageVector =
            appIcon("StarHalf") {
                moveTo(22f, 9.24f)
                lineTo(14.81f, 8.62f)
                lineTo(12f, 2f)
                lineTo(9.19f, 8.63f)
                lineTo(2f, 9.24f)
                lineTo(7.46f, 13.97f)
                lineTo(5.82f, 21f)
                lineTo(12f, 17.27f)
                lineTo(18.18f, 21f)
                lineTo(16.55f, 13.97f)
                lineTo(22f, 9.24f)
                close()
                moveTo(12f, 15.4f)
                verticalLineTo(6.1f)
                lineTo(13.71f, 10.14f)
                lineTo(18.09f, 10.52f)
                lineTo(14.77f, 13.4f)
                lineTo(15.77f, 17.68f)
                lineTo(12f, 15.4f)
                close()
            }
        val StarBorder: ImageVector =
            appIcon("StarBorder") {
                moveTo(22f, 9.24f)
                lineTo(14.81f, 8.62f)
                lineTo(12f, 2f)
                lineTo(9.19f, 8.63f)
                lineTo(2f, 9.24f)
                lineTo(7.46f, 13.97f)
                lineTo(5.82f, 21f)
                lineTo(12f, 17.27f)
                lineTo(18.18f, 21f)
                lineTo(16.55f, 13.97f)
                lineTo(22f, 9.24f)
                close()
                moveTo(12f, 15.4f)
                lineTo(8.24f, 17.67f)
                lineTo(9.24f, 13.39f)
                lineTo(5.92f, 10.51f)
                lineTo(10.3f, 10.13f)
                lineTo(12f, 6.1f)
                lineTo(13.71f, 10.14f)
                lineTo(18.09f, 10.52f)
                lineTo(14.77f, 13.4f)
                lineTo(15.77f, 17.68f)
                lineTo(12f, 15.4f)
                close()
            }
}

private fun appIcon(name: String, block: PathBuilder.() -> Unit): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black), name = name, pathBuilder = block)
    }.build()

@Preview(showBackground = true)
@Composable
private fun AppIconsPreview() {
    val icons = listOf(
        AppIcons.Home,
        AppIcons.History,
        AppIcons.Wallet,
        AppIcons.Offer,
        AppIcons.Person,
        AppIcons.Search,
        AppIcons.Star,
        AppIcons.StarHalf,
        AppIcons.StarBorder,
    )
    AppTheme {
        Row(
            modifier = Modifier.padding(AppSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            icons.forEach { icon ->
                Icon(icon, contentDescription = icon.name, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

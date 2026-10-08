package com.rideplatform.blueride.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rideplatform.blueride.presentation.theme.AppSpacing
import com.rideplatform.blueride.presentation.theme.AppTheme
import com.rideplatform.blueride.presentation.theme.PillShape
import com.rideplatform.blueride.presentation.theme.Success
import com.rideplatform.blueride.presentation.theme.Warning

@Composable
fun StatusPill(
    text: String,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Surface(
        modifier = modifier,
        shape = PillShape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = dotColor, shape = CircleShape),
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatusPillPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            StatusPill(text = "Tài xế đang đến", dotColor = Success)
            StatusPill(text = "Đang tìm tài xế", dotColor = Warning)
            StatusPill(text = "Hoàn thành")
        }
    }
}

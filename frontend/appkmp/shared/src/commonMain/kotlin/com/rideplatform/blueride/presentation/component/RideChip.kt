package com.rideplatform.blueride.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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

@Composable
fun RideChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = PillShape,
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceContainerLowest,
        contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        border = BorderStroke(
            if (selected) 1.5.dp else 1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
        ) {
            icon?.invoke()
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.W500),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RideChipPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                RideChip(text = "Tiền mặt", selected = true, onClick = {})
                RideChip(text = "Ví", selected = false, onClick = {})
                RideChip(
                    text = "Ưu đãi",
                    selected = false,
                    onClick = {},
                    icon = { Icon(AppIcons.Offer, contentDescription = null, modifier = Modifier.size(18.dp)) },
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                RideChip(text = "Đã chọn", selected = true, onClick = {}, enabled = false)
                RideChip(text = "Tắt", selected = false, onClick = {}, enabled = false)
            }
        }
    }
}

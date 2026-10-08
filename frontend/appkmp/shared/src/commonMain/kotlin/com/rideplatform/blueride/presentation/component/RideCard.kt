package com.rideplatform.blueride.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rideplatform.blueride.presentation.theme.AppSpacing
import com.rideplatform.blueride.presentation.theme.AppTheme

@Composable
fun RideCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    )
    val cardElevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    val cardContent: @Composable ColumnScope.() -> Unit = {
        Column(modifier = Modifier.padding(contentPadding), content = content)
    }
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
            colors = cardColors,
            elevation = cardElevation,
            content = cardContent,
        )
    } else {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
            colors = cardColors,
            elevation = cardElevation,
            content = cardContent,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RideCardPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            RideCard(modifier = Modifier.fillMaxWidth()) {
                Text("BlueBike", style = MaterialTheme.typography.titleMedium)
                Text("Đón trong 3 phút", style = MaterialTheme.typography.bodyMedium)
            }
            RideCard(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("BlueCar 4 chỗ", style = MaterialTheme.typography.titleMedium)
                    Text("45.000đ", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

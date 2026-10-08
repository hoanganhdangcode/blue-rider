package com.rideplatform.blueride.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rideplatform.blueride.presentation.theme.Warning

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Int = 20,
    onRatingChange: ((Float) -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        for (index in 1..5) {
            val fraction = (rating - (index - 1)).coerceIn(0f, 1f)
            val iconModifier = Modifier.size(starSize.dp).let { base ->
                if (onRatingChange != null) {
                    val indexFloat = index.toFloat()
                    base.clickable { onRatingChange(indexFloat) }
                } else {
                    base
                }
            }
            Icon(
                imageVector = when {
                    fraction >= 1f -> AppIcons.Star
                    fraction >= 0.5f -> AppIcons.StarHalf
                    else -> AppIcons.StarBorder
                },
                contentDescription = null,
                modifier = iconModifier,
                tint = if (fraction > 0f) Warning else MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}

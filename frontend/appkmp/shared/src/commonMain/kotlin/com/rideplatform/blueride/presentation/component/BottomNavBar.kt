package com.rideplatform.blueride.presentation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rideplatform.blueride.presentation.theme.Primary

data class BottomNavBarItem(
    val label: String,
    val icon: ImageVector,
)

object BottomNavBarDefaults {
    val items = listOf(
        BottomNavBarItem("Trang chủ", AppIcons.Home),
        BottomNavBarItem("Lịch sử", AppIcons.History),
        BottomNavBarItem("Ví", AppIcons.Wallet),
        BottomNavBarItem("Ưu đãi", AppIcons.Offer),
        BottomNavBarItem("Hồ sơ", AppIcons.Person),
    )
}

@Composable
fun RideBottomNavBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<BottomNavBarItem> = BottomNavBarDefaults.items,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        tonalElevation = 0.dp,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onSelect(index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                ),
            )
        }
    }
}

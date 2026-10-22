package com.rideplatform.rideui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rideplatform.rideui.booking.BookingScreen
import com.rideplatform.rideui.home.HomeScreen
import com.rideplatform.rideui.theme.RideUiTheme

private sealed interface Route {
    data object Home : Route
    data object Booking : Route
}

@Composable
fun RideApp() {
    RideUiTheme {
        var route by remember { mutableStateOf<Route>(Route.Home) }
        when (route) {
            Route.Home -> HomeScreen(
                onFindRide = { route = Route.Booking },
            )
            Route.Booking -> BookingScreen(
                onBack = { route = Route.Home },
            )
        }
    }
}

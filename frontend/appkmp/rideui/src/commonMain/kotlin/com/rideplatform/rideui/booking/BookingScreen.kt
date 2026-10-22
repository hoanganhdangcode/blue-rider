package com.rideplatform.rideui.booking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rideplatform.ridecore.booking.RideOffer

private val sampleOffers = listOf(
    RideOffer(id = "1", vehicleName = "Bike", price = 15_000, currency = "VND", etaSeconds = 180),
    RideOffer(id = "2", vehicleName = "Car", price = 45_000, currency = "VND", etaSeconds = 300),
    RideOffer(id = "3", vehicleName = "XL", price = 70_000, currency = "VND", etaSeconds = 420),
)

@Composable
fun BookingScreen(
    onBack: () -> Unit,
) {
    val offers = remember { sampleOffers }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }
        Text(
            text = "Choose a ride",
            style = MaterialTheme.typography.titleLarge,
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(offers, key = { it.id }) { offer ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(offer.vehicleName, style = MaterialTheme.typography.titleLarge)
                            Text(
                                "ETA ${offer.etaSeconds / 60} min",
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                        Text(
                            "${offer.price} ${offer.currency}",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

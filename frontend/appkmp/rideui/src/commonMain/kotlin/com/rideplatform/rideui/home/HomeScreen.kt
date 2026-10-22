package com.rideplatform.rideui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onFindRide: () -> Unit,
) {
    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = "BlueRide",
            style = MaterialTheme.typography.headlineMedium,
        )
        OutlinedTextField(
            value = pickup,
            onValueChange = { pickup = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Pickup") },
            singleLine = true,
        )
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Destination") },
            singleLine = true,
        )
        Button(
            onClick = onFindRide,
            modifier = Modifier.fillMaxWidth(),
            enabled = pickup.isNotBlank() && destination.isNotBlank(),
        ) {
            Text("Find a ride")
        }
    }
}

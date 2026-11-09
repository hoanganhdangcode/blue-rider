package com.rideplatform.blueride.data.local.datasource

import androidx.room3.RoomDatabase

expect class DatabaseBuilderFactory {
    fun create(): RoomDatabase.Builder<AppDatabase>
}

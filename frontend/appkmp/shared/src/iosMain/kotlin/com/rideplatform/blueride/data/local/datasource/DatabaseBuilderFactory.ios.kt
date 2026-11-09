package com.rideplatform.blueride.data.local.datasource

import androidx.room3.Room
import androidx.room3.RoomDatabase
import org.koin.core.annotation.Single
import platform.Foundation.NSHomeDirectory

@Single
actual class DatabaseBuilderFactory {
    actual fun create(): RoomDatabase.Builder<AppDatabase> {
        val dbFilePath = NSHomeDirectory() + "/app_database.db"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
    }
}

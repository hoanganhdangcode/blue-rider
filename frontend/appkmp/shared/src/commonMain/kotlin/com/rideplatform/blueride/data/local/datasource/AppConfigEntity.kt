package com.rideplatform.blueride.data.local.datasource

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "app_config")
data class AppConfigEntity(
    @PrimaryKey val key: String,
    val value: String,
)

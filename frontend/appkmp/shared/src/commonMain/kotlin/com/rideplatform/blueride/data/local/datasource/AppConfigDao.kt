package com.rideplatform.blueride.data.local.datasource

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface AppConfigDao {
    @Upsert
    suspend fun upsert(config: AppConfigEntity)

    @Query("SELECT * FROM app_config WHERE `key` = :key")
    suspend fun get(key: String): AppConfigEntity?
}

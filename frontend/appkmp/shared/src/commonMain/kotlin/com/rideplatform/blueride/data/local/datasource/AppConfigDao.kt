package com.rideplatform.blueride.data.local.datasource

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface AppConfigDao {

    @Query("SELECT * FROM app_config WHERE `key` = :key")
    suspend fun get(key: String): AppConfigEntity?

    @Upsert
    suspend fun set(config: AppConfigEntity)

    @Query("DELETE FROM app_config WHERE `key` = :key")
    suspend fun delete(key: String)
}
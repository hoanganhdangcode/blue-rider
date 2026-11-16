package com.rideplatform.blueride.domain.repository.local

interface AppConfigRepository {
    suspend fun getAppConfig(k: String):String?
    suspend fun setAppConfig(k: String, v:String)
    suspend fun deleteAppConfig(k: String)
}
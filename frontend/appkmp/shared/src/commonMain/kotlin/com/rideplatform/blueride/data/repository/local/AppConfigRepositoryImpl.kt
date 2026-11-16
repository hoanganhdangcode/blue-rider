package com.rideplatform.blueride.data.repository.local

import com.rideplatform.blueride.data.local.datasource.AppConfigDao
import com.rideplatform.blueride.data.local.datasource.AppConfigEntity
import com.rideplatform.blueride.domain.repository.local.AppConfigRepository
import org.koin.core.annotation.Single

@Single
class AppConfigRepositoryImpl(private val dao: AppConfigDao): AppConfigRepository {
    override suspend fun getAppConfig(k: String): String? {
        return (dao.get(k)?.value)
    }

    override suspend fun setAppConfig(k: String, v: String) {
        dao.set(AppConfigEntity(key = k, value = v))
    }

    override suspend fun deleteAppConfig(k: String) {
        dao.delete(k)
    }
}
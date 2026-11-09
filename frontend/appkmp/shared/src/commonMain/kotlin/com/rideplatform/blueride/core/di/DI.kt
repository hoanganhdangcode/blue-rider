package com.rideplatform.blueride.core.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.rideplatform.blueride.core.config.BuildEnv
import com.rideplatform.blueride.core.network.HttpClientFactory
import com.rideplatform.blueride.data.local.datasource.AppConfigDao
import com.rideplatform.blueride.data.local.datasource.AppDatabase
import com.rideplatform.blueride.data.local.datasource.DatabaseBuilderFactory
import io.ktor.client.HttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.startKoin

@Module
@ComponentScan("com.rideplatform.blueride.data")
class DataModule {
    @Single
    fun provideHttpClient(): HttpClient = HttpClientFactory(
        baseUrl = BuildEnv.BASE_URL,
        enableHttpLog = BuildEnv.ENABLE_HTTP_LOG,
        debug = BuildEnv.DEBUG,
    ).create()
}

@Module
@ComponentScan("com.rideplatform.blueride.domain")
class DomainModule

@Module
@ComponentScan("com.rideplatform.blueride.presentation")
class PresentationModule


@Module
class DatabaseModule {

    @Single
    fun provideAppDatabase(factory: DatabaseBuilderFactory): AppDatabase =
        factory.create()
            .setDriver(BundledSQLiteDriver())
            .build()

    @Single
    fun provideAppConfigDao(database: AppDatabase): AppConfigDao =
        database.appConfigDao()
}


@KoinApplication(modules = [DataModule::class, DomainModule::class, PresentationModule::class, DatabaseModule::class])
class AppKoin

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin<AppKoin> {
        config?.invoke(this)
    }
}

package com.rideplatform.blueride.core.di

import com.rideplatform.blueride.core.config.BuildEnv
import com.rideplatform.blueride.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
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
    @Single
    @Named("abc")
    fun abc():String = "abc"
    @Single
    @Named("bcd")
    fun bcd():String = "bcd"
}

@Module
@ComponentScan("com.rideplatform.blueride.domain")
class DomainModule

@Module
@ComponentScan("com.rideplatform.blueride.presentation")
class PresentationModule

@KoinApplication(modules = [DataModule::class, DomainModule::class, PresentationModule::class])
class AppKoin
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin<AppKoin> {
        config?.invoke(this)
    }
}

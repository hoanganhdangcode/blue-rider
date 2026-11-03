package com.rideplatform.blueride.core.di

import org.koin.core.annotation.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

@KoinApplication(modules = [])
class AppKoin{
    fun initKoin(
        config: KoinAppDeclaration? = null
    ) {
        startKoin {
            config?.invoke(this)
            modules(
//            DataModule().module,
//            DomainModule().module,
//            PresentationModule().module
            )
        }
    }
}

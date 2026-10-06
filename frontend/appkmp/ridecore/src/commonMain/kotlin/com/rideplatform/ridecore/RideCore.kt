package com.rideplatform.ridecore

import com.rideplatform.ridecore.api.ApiClient
import com.rideplatform.ridecore.api.ApiConfig
import com.rideplatform.ridecore.api.HttpApiClient
import com.rideplatform.ridecore.booking.DefaultRideRepository
import com.rideplatform.ridecore.booking.RideRepository

object RideCore {

    lateinit var config: ApiConfig
        private set

    lateinit var apiClient: ApiClient
        private set

    val rideRepository: RideRepository by lazy { DefaultRideRepository(apiClient) }

    fun initialize(config: ApiConfig) {
        this.config = config
        this.apiClient = HttpApiClient(config)
    }

    internal fun requireInitialized() {
        check(this::apiClient.isInitialized) {
            "RideCore is not initialized. Call RideCore.initialize(config) first."
        }
    }
}

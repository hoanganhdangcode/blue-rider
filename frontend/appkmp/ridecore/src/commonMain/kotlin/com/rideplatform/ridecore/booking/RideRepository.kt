package com.rideplatform.ridecore.booking

import com.rideplatform.ridecore.api.ApiResult

interface RideRepository {
    suspend fun searchOffers(request: RideRequest): ApiResult<List<RideOffer>>
    suspend fun book(offerId: String, request: RideRequest): ApiResult<String>
    suspend fun getStatus(tripId: String): ApiResult<RideStatus>
    suspend fun cancel(tripId: String): ApiResult<Unit>
}

package com.rideplatform.ridecore.booking

import com.rideplatform.ridecore.api.ApiClient
import com.rideplatform.ridecore.api.ApiResult

class DefaultRideRepository(private val apiClient: ApiClient) : RideRepository {

    override suspend fun searchOffers(request: RideRequest): ApiResult<List<RideOffer>> =
        ApiResult.Failure(code = null, message = "searchOffers not implemented yet")

    override suspend fun book(offerId: String, request: RideRequest): ApiResult<String> =
        ApiResult.Failure(code = null, message = "book not implemented yet")

    override suspend fun getStatus(tripId: String): ApiResult<RideStatus> =
        ApiResult.Failure(code = null, message = "getStatus not implemented yet")

    override suspend fun cancel(tripId: String): ApiResult<Unit> =
        ApiResult.Failure(code = null, message = "cancel not implemented yet")
}

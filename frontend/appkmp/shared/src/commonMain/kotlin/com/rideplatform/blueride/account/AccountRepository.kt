package com.rideplatform.blueride.account

import com.rideplatform.ridecore.api.ApiClient
import com.rideplatform.ridecore.api.ApiResult

class AccountRepository(private val apiClient: ApiClient) {

    suspend fun getProfile(): ApiResult<String> =
        ApiResult.Failure(code = null, message = "getProfile not implemented yet")
}

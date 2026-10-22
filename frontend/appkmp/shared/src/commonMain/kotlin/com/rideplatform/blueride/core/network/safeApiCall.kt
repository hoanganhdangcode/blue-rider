package com.rideplatform.blueride.core.network

import com.rideplatform.blueride.core.error.AppError
import com.rideplatform.blueride.core.error.AppResult
import com.rideplatform.blueride.core.error.ErrorMapper
import com.rideplatform.blueride.core.util.NetworkUtils
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(block: suspend () -> ApiResponse<T>): AppResult<T?> {
    val response = try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        return AppResult.Error(ErrorMapper.map(e))
    }
    return when (response.code) {
        NetworkUtils.API_SUCCESS_CODE -> AppResult.Success(response.data)
        NetworkUtils.API_EXPIRE_CODE -> AppResult.Error(AppError.SessionExpired)
        else -> AppResult.Error(AppError.Business(response.code, response.message))
    }
}



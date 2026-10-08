package com.rideplatform.blueride.core.network

import com.rideplatform.blueride.core.util.CoreUtils
import kotlinx.serialization.Serializable

@Serializable
data class ResponseEnvelope<T>(
    val code: String,
    val data: T? = null,
    val message: String? = null,
)

fun <T> ResponseEnvelope<T>.isSuccess(): Boolean {
    return CoreUtils.API_SUCCESS_CODE == this.code
}
fun <T> ResponseEnvelope<T>.isExpire(): Boolean {
    return CoreUtils.API_EXPIRE_CODE == this.code
}

package com.rideplatform.blueride.core.error

sealed class AppError {
    //Không kết nối được: mất mạng, timeout, DNS...
    data class Network(val message: String?, val throwable: Throwable? = null) : AppError()

    // HTTP 4xx: lỗi client
    data class Client(val httpStatus: Int, val message: String?) : AppError()

    // HTTP 5xx: lỗi server
    data class Server(val httpStatus: Int, val message: String?) : AppError()

    // HTTP 200 nhưng envelope trả code nghiệp vụ khác thành công.
    data class Business(val code: String, val message: String?) : AppError()

    data object SessionExpired : AppError()

    data class Unknown(val message: String?, val throwable: Throwable? = null) : AppError()
}
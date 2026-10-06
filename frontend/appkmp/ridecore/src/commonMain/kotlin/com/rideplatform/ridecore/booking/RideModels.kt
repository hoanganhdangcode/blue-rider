package com.rideplatform.ridecore.booking

data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
)

data class RideRequest(
    val pickup: GeoPoint,
    val destination: GeoPoint,
    val paymentMethodId: String? = null,
)

data class RideOffer(
    val id: String,
    val vehicleName: String,
    val price: Long,
    val currency: String,
    val etaSeconds: Int,
)

enum class RideStatus {
    SEARCHING,
    MATCHED,
    DRIVER_ARRIVING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
}

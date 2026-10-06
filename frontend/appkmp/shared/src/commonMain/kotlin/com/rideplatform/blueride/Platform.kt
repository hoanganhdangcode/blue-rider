package com.rideplatform.blueride

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
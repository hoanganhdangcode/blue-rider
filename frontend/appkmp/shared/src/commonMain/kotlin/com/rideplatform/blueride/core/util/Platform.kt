package com.rideplatform.blueride.core.util

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
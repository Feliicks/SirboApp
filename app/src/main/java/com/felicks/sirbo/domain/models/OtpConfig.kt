package com.felicks.sirbo.domain.models

data class OtpConfig(
    val mode: String = "TRANSIT,WALK",
    val walkDistance: Int = 1000,
    val maxTransfers: Int = 1,
    val numItineraries: Int = 3
)

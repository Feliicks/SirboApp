package com.felicks.sirbo.data.models.otpModels.routing

import androidx.annotation.Keep

@Keep
data class ElevationMetadata(
    val ellipsoidToGeoidDifference: Double,
    val geoidElevation: Boolean
)
package com.felicks.sirbo.data.models.otpModels.routing

import androidx.annotation.Keep

@Keep
data class DebugOutput(
    val pathCalculationTime: Int,
    val pathTimes: List<Int>,
    val precalculationTime: Int,
    val renderingTime: Int,
    val timedOut: Boolean,
    val totalTime: Int
)
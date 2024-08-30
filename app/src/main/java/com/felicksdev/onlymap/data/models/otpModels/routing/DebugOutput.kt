package com.felicksdev.onlymap.data.models.otpModels.routing

data class DebugOutput(
    val pathCalculationTime: Int,
    val pathTimes: List<Int>,
    val precalculationTime: Int,
    val renderingTime: Int,
    val timedOut: Boolean,
    val totalTime: Int
)
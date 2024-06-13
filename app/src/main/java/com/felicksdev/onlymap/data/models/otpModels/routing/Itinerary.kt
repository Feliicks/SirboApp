package com.felicksdev.onlymap.data.models.otpModels.routing

data class Itinerary(
    val duration: Int,
    val elevationGained: Double,
    val elevationLost: Double,
    val endTime: Long,
    val legs: List<Leg>,
    val startTime: Long,
    val tooSloped: Boolean,
    val transfers: Int,
    val transitTime: Int,
    val waitingTime: Int,
    val walkDistance: Double,
    val walkLimitExceeded: Boolean,
    val walkTime: Int
)
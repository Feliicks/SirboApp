package com.felicksdev.onlymap.data.models.otpModels.routing

data class Plan(
    val date: Long,
    val from: From,
    val itineraries: List<Itinerary>,
    val to: ToX
)
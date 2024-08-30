package com.felicksdev.onlymap.data.models.otpModels

data class PatterDetail(
    val id : String,
    val desc: String,
    val routeId: String,
    val stops: List<RouteStopItem>
)

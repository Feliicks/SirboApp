package com.felicks.sirbo.data.models.otpModels

data class PatterDetail(
    var id: String = "",
    var desc: String = "",
    var routeId: String = "",
    var stops: List<RouteStopItem> = emptyList()
)

package com.felicks.sirbo.data.models.otpModels

import com.felicks.sirbo.data.local.entity.patternDetail.PatternDetailEntity

data class PatterDetail(
    var id: String = "",
    var desc: String = "",
    var routeId: String = "",
    var stops: List<RouteStopItem> = emptyList(),
    val trips: List<Trip> = emptyList(),
)
// Dominio → Entidad
fun PatterDetail.toEntity(): PatternDetailEntity {
    return PatternDetailEntity(
        id = this.id,
        desc = this.desc,
        routeId = this.routeId
    )
}
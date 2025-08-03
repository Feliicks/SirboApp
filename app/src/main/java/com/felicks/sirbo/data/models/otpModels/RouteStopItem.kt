package com.felicks.sirbo.data.models.otpModels

import androidx.annotation.Keep
import com.felicks.sirbo.data.local.entity.patternDetail.StopEntity

@Keep
data class RouteStopItem(
    val id: String,
    val lat: Double,
    val lon: Double,
    val name: String
)

// RouteStopItem → StopEntity
fun RouteStopItem.toEntity(patternId: String): StopEntity {
    return StopEntity(
        id = this.id,
        name = this.name,
        lat = this.lat,
        lon = this.lon,
        patternId = patternId
    )
}
// Lists
fun List<RouteStopItem>.toEntityList(patternId: String): List<StopEntity> {
    return this.map { it.toEntity(patternId) }
}
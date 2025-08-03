package com.felicks.sirbo.data.models.otpModels

import com.felicks.sirbo.data.local.entity.patternDetail.TripEntity

data class Trip(
    val id: String,
    val serviceId: String,
    val shapeId: String
)

fun Trip.toEntity(patternId: String): TripEntity {
    return TripEntity(
        id = this.id,
        serviceId = this.serviceId,
        shapeId = this.shapeId,
        patternId = patternId
    )
}

fun List<Trip>.toEntityList(patternId: String): List<TripEntity> {
    return this.map { it.toEntity(patternId) }
}
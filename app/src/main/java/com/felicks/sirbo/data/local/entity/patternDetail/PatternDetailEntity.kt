package com.felicks.sirbo.data.local.entity.patternDetail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.models.otpModels.PatterDetail

@Entity(tableName = DBConstants.TABLA_PATTERNS_DETALLE)
data class PatternDetailEntity(
    @PrimaryKey val id: String,
    val desc: String,
    val routeId: String
)

fun PatternDetailEntity.toDomain(
    stops: List<StopEntity>,
    trips: List<TripEntity>
): PatterDetail {
    return PatterDetail(
        id = this.id,
        desc = this.desc,
        routeId = this.routeId,
        stops = stops.map { it.toDomain() },
        trips = trips.map { it.toDomain() }
    )
}
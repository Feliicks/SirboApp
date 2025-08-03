package com.felicks.sirbo.data.local.entity.patternDetail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.models.otpModels.Trip


@Entity(tableName = DBConstants.TABLA_TRIPS)
data class TripEntity(
    @PrimaryKey val id: String,
    val serviceId: String,
    val shapeId: String,
    val patternId: String // foreign key implícita
)

// TripEntity → Trip
fun TripEntity.toDomain(): Trip = Trip(
    id = id,
    serviceId = serviceId,
    shapeId = shapeId
)
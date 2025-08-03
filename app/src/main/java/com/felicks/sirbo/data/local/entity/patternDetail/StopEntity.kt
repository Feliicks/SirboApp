package com.felicks.sirbo.data.local.entity.patternDetail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.models.otpModels.RouteStopItem

@Entity(tableName = DBConstants.TABLA_STOPS)
data class StopEntity(
    @PrimaryKey val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val patternId: String // foreign key implícita
)

// StopEntity → RouteStopItem
fun StopEntity.toDomain(): RouteStopItem = RouteStopItem(
    id = id,
    name = name,
    lat = lat,
    lon = lon
)

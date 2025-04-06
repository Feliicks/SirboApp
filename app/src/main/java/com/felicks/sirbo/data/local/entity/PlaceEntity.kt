package com.felicks.sirbo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(

    @PrimaryKey var id: String, // 🔑 ID autogenerado
    var name: String,   // 📍 Nombre del lugar
    var latitude: Double,  // 🌍 Latitud del lugar
    var longitud: Double,  // 🌍 Longitud del lugar
    var createdAt: Long,
    var updatedAt: Long,
    var lastUsedAt: Long,
    var isFavorites: Boolean
)
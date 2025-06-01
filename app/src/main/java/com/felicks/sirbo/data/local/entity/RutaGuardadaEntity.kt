package com.felicks.sirbo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "rutas_guardadas")
data class RutaGuardadaEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val usuarioId: String,
    val nombreRuta: String,
    val origenLat: Double,
    val origenLon: Double,
    val direccionOrigen: String,
    val destinoLat: Double,
    val destinoLon: Double,
    val direccionDestino: String,
    val geometriaGeoJson: String,
    val fechaGuardado: String,
    val fechaUltimoUso: String
)


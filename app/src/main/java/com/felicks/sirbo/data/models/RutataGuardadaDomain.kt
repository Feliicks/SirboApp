package com.felicks.sirbo.data.models

import com.felicks.sirbo.data.local.entity.RutaGuardadaEntity
import com.felicks.sirbo.utils.FechaUtils
import java.util.Date
import java.util.UUID

data class RutaGuardadaDomain(
    val usuarioId: String,
    val nombreRuta: String,
    val origenLat: Double,
    val origenLon: Double,
    val destinoLat: Double,
    val destinoLon: Double,
    val direccionOrigen: String,
    val direccionDestino: String,
    val geoJson: String,
    val fechaGuardado: Date,
    val fechaUltimoUso: Date,
)

fun RutaGuardadaDomain.toEntity(): RutaGuardadaEntity {
    return RutaGuardadaEntity(
        id = UUID.randomUUID().toString(),
        usuarioId = usuarioId,
        nombreRuta = nombreRuta,
        origenLat = origenLat,
        origenLon = origenLon,
        destinoLat = destinoLat,
        destinoLon = destinoLon,
        direccionOrigen = direccionOrigen,
        direccionDestino = direccionDestino,
        geometriaGeoJson = geoJson,
        fechaGuardado = FechaUtils.dateToString(fechaGuardado),
        fechaUltimoUso = FechaUtils.dateToString(fechaUltimoUso),
    )
}
fun RutaGuardadaEntity.toDomain(): RutaGuardadaDomain {
    return RutaGuardadaDomain(
        usuarioId = usuarioId,
        nombreRuta = nombreRuta,
        origenLat = origenLat,
        origenLon = origenLon,
        destinoLat = destinoLat,
        destinoLon = destinoLon,
        direccionOrigen = direccionOrigen,
        direccionDestino = direccionDestino,
        geoJson = geometriaGeoJson,
        fechaGuardado = FechaUtils.stringToDate(fechaGuardado) ?: Date(),
        fechaUltimoUso = FechaUtils.stringToDate(fechaUltimoUso) ?: Date()
    )
}


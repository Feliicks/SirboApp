package com.felicks.sirbo.domain

data class Place(
    var name: String = "",
    var latitud: Double = 0.0,
    var longitud: Double = 0.0,
    var fechaCreacion: Long = 0,
    var fechaActualizacion: Long = 0,
    var fechaUltimoUso: Long = 0,
    var isFavorite: Boolean = false,
)


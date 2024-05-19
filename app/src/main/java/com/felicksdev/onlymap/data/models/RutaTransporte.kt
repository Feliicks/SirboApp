package com.felicksdev.onlymap.data.models

data class RutaTransporte(
    val codigoRuta: String,
    val ruta: String,
    val tipoRuta: String,
    val tipoVehiculo: String,
    val sentido: String,
    val recorrido: String,
    val operabilidad: String,
    val operador: String,
    val entidadMatriz: String,
    val aprobacion: String
)

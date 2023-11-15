package com.felicksdev.onlymap.models

data class RutaVehicularItem(
    val cod_ruta: String,
    val geometria_ruta: GeometriaRuta,
    val id: Int,
    val nombre: String,
    val recorrido: String,
    val ruta_anterior: String
)
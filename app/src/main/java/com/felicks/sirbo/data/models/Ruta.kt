package com.felicks.sirbo.data.models

data class Ruta(
    val cod_ruta: String,
    val geometria_ruta: GeometriaRuta,
    val id: Int,
    val nombre: String,
    val recorrido: String,
    val ruta_anterior: String,
    val sentido_ruta: SentidoRuta,
    val tipo_ruta: TipoRuta,
    val operador: Operador,
    val tipo_vehiculo: TipoVehiculo
)

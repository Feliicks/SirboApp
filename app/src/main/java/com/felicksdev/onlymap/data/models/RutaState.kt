package com.felicksdev.onlymap.data.models

data class RutaState (
    val cod_ruta: String= "",
    val geometria_ruta: GeometriaRuta = GeometriaRuta(coordinates = listOf(listOf(listOf(0.0, 0.0))), type = "", geoJsonString = ""),
    val id: Int = 0,
    val nombre: String = "",
    val recorrido: String = "",
    val ruta_anterior: String = "",
    val sentido_ruta: SentidoRuta = SentidoRuta(id = 0, sentido = ""),
    val tipo_ruta: TipoRuta = TipoRuta(id = 0, tipo_ruta = ""),
    val operador: Operador = Operador(id = 0, nombre_sindicato = ""),
    val tipo_vehiculo: TipoVehiculo = TipoVehiculo(id = 0, tipo_vehiculo = "")
)
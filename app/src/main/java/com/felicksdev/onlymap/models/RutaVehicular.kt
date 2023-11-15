package com.felicksdev.onlymap.models

class RutaVehicular : ArrayList<Ruta>() {
    fun getRutaVehicularItem(): Ruta {
        return Ruta(
            cod_ruta = "",
            geometria_ruta = GeometriaRuta(
                coordinates = listOf(listOf(listOf(0.0))),
                type = "",
                geoJsonString = ""
            ),
            id = 0,
            nombre = "",
            recorrido = "",
            ruta_anterior = ""
        )
    }
}
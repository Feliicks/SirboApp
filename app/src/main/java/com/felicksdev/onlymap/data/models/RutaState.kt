package com.felicksdev.onlymap.data.models

import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem

//data class RutaState (
//    val cod_ruta: String= "",
//    val nombre: String = "",
//    val recorrido: String = "",
//    val ruta_anterior: String = "",
//    val sentido_ruta: SentidoRuta = SentidoRuta(id = 0, sentido = ""),
//    val tipo_ruta: TipoRuta = TipoRuta(id = 0, tipo_ruta = ""),
//    val operador: Operador = Operador(id = 0, nombre_sindicato = ""),
//    val tipo_vehiculo: TipoVehiculo = TipoVehiculo(id = 0, tipo_vehiculo = "")
//)
data class RutaState (
   val rutas : List<RoutesModelItem> = emptyList(),
   val currentRuta : List<RoutesModelItem> = emptyList()
)
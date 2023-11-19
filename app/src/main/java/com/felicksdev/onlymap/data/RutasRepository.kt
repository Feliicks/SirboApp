package com.felicksdev.onlymap.data

import com.felicksdev.onlymap.RutaProvider
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.network.MyApiService
import com.felicksdev.onlymap.data.network.RouteServices

class RutasRepository {
    private val api = RouteServices()
    suspend fun getRutas() : List<Ruta> {
        val response = api.getRoutes()
        RutaProvider.rutas = response
        return response
    }
}
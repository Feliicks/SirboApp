package com.felicksdev.onlymap.domain.model

import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.services.network.RouteServices

class RutasRepository {
    private val api = RouteServices()
    suspend fun getRutas() : List<Ruta> {
        val response = api.getRoutes()
        RutaProvider.rutas = response
        return response
    }
}
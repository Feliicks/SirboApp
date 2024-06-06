package com.felicksdev.onlymap.domain.model

import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem
import com.felicksdev.onlymap.services.network.RouteServices

class RutasRepository {
    private val api = RouteServices()
    suspend fun getRutas() : List<RoutesModelItem> {
        val response = api.getRoutes()
//        RutaProvider.rutas = response
        return response
    }
}
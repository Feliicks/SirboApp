package com.felicksdev.onlymap.services.network

import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.services.network.RetrofitHelper.getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteServices  {
    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun getRoutes(): List<RoutesItem> {
        return withContext(Dispatchers.IO){
            val response = getRetrofit().indexRoutes()
            response.body() ?: emptyList()
        }
    }

}
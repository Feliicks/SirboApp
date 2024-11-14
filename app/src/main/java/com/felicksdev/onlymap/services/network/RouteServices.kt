package com.felicksdev.onlymap.services.network

import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.services.network.RetrofitHelper.otpRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteServices  {
    private val retrofit = RetrofitHelper.otpRetrofit()
    suspend fun getRoutes(): List<RoutesItem> {
        return withContext(Dispatchers.IO){
            val response = otpRetrofit().indexRoutes()
            response.body() ?: emptyList()
        }
    }

}
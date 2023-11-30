package com.felicksdev.onlymap.data.network

import com.felicksdev.onlymap.core.RetrofitHelper
import com.felicksdev.onlymap.core.RetrofitHelper.getRetrofit
import com.felicksdev.onlymap.data.models.Ruta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteServices  {
    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun getRoutes(): List<Ruta> {
        return withContext(Dispatchers.IO){
            val response = getRetrofit().getAllRutas()
            response.body() ?: emptyList()
        }
    }

}
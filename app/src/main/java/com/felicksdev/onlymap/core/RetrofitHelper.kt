package com.felicksdev.onlymap.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            //.baseUrl("http://10.0.2.2:3000/")
            .baseUrl("https://116a-186-121-247-163.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
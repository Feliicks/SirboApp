package com.felicks.sirbo.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object RetrofitInstance {
//    private var retrofit: Retrofit? = null
//
//    fun getInstance(baseUrl: String): Retrofit {
//        if (retrofit == null || retrofit?.baseUrl().toString() != baseUrl) {
//            retrofit = Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        }
//        return retrofit!!
//    }
//}

package com.felicks.sirbo.services

//object RetrofitManager {
//
//    private var retrofit: Retrofit? = null
//    private var baseUrl: String = ""
//
//    fun getApiService(): MyApiService {
//        if (retrofit == null) {
//            throw IllegalStateException("Retrofit not initialized. Call initRetrofit() first.")
//        }
//        return retrofit!!.create(MyApiService::class.java)
//    }
//
//    fun initRetrofit(newBaseUrl: String) {
//        if (retrofit != null && baseUrl == newBaseUrl) return // ya está inicializado
//
//        baseUrl = newBaseUrl
//        retrofit = Retrofit.Builder()
//            .baseUrl(newBaseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    fun getBaseUrl(): String = baseUrl
//}

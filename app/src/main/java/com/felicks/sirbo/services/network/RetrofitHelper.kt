package com.felicks.sirbo.services.network

//
//object RetrofitHelper {
//
//    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
//        .connectTimeout(5, TimeUnit.SECONDS)
//        .readTimeout(5, TimeUnit.SECONDS)
//        .build()
//
//    fun otpRetrofit(): OtpService {
//        return Retrofit.Builder()
////            .baseUrl("http://10.0.2.2:8080/")
//            .baseUrl("http://192.168.1.216:8080/")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(OtpService::class.java)
//    }
//
//    // Retrofit instance for Photon Komoot
//    private val photonRetrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://api.photon.komoot.io") // Cambia a tu URL
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//}
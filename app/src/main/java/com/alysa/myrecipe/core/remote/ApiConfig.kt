//package com.alysa.myrecipe.core.remote
//
//import android.content.Context
//import android.icu.lang.UCharacter.GraphemeClusterBreak.T
//import com.chuckerteam.chucker.api.ChuckerCollector
//import com.chuckerteam.chucker.api.ChuckerInterceptor
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class ApiConfig private constructor() {
//
//    companion object {
//        inline fun getApiService(context: Context, apiType: String): Any? {
//            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
//                .collector(ChuckerCollector(context))
//                .maxContentLength(250000L)
//                .redactHeaders(emptySet())
//                .alwaysReadResponseBody(false)
//                .build()
//
//            val API_BASE_URL = "http://192.168.0.114:3000/api/v1/"
//
//            val retrofit = Retrofit.Builder()
//                .baseUrl(API_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
////            val authApi = retrofit.create(AuthApi::class.java)
//
//            val client = OkHttpClient.Builder()
//                .addInterceptor(chuckerInterceptor)
//                .build()
//
//            return retrofit.newBuilder().client(client).build().create(T::class.java)
//        }
//    }
//}
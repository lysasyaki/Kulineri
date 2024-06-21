package com.alysa.myrecipe.core.remote

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig private constructor() {

    companion object {
        inline fun getApiService(context: Context, apiType: String): Any? {
            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()

            val API_BASE_URL = "http://192.168.53.87:3000/api/v1/"

            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(chuckerInterceptor)
                .build()

            return when (apiType) {
                "signIn" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceSignIn::class.java)
                }
                "signUp" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceSignUp::class.java)
                }
//                "recipe" -> {
//                    retrofit.newBuilder().client(client).build().create(ApiServiceRecipe::class.java)
//                }
                else -> throw IllegalArgumentException("Unknown API type: $apiType")
            }
        }
    }
}
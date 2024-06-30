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
                "recipeType" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceRecipeType::class.java)
                }
                "recipeMakanan" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceRecipeMakanan::class.java)
                }
                "detailRecipe" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceDetail::class.java)
                }
                "addFavorite" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceFavorite::class.java)
                }
                "signOut" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceSignOut::class.java)
                }
                "deleteFavorite" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceDeleteFavorite::class.java)
                }
                "uploadRecipe" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceUpload::class.java)
                }
                "getFavorite" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceGetFavorite::class.java)
                }
                "getRecipe" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceRecipeByUser::class.java)
                }
                "deleteRecipe" -> {
                    retrofit.newBuilder().client(client).build().create(ApiServiceDeleteRecipe::class.java)
                }
                else -> throw IllegalArgumentException("Unknown API type: $apiType")
            }
        }
    }
}
package com.alysa.myrecipe.core.remote

import com.alysa.myrecipe.core.domain.auth.RequestSignIn
import com.alysa.myrecipe.core.domain.auth.RequestSignUp
import com.alysa.myrecipe.core.domain.auth.ResponseSignIn
import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
//import com.alysa.myrecipe.core.domain.recipe.ResponseGetRecipe
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServiceSignIn {
    @POST("signIn")
    fun postSignIn(
        @Body requestSignIn: RequestSignIn
    ): Call<ResponseSignIn>
}

interface ApiServiceSignUp {
    @POST("SignUp")
    fun postSignUp(
        @Body requestSignUp: RequestSignUp
    ): Call<ResponseSignUp>
}

//interface ApiServiceRecipe {
//    @GET("api/v1/")
//    fun getRecipe(
//        @Query("recipe") recipe: String,
//    ): Call<List<ResponseGetRecipe>>
//}
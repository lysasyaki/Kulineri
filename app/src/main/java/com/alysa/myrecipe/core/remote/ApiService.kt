package com.alysa.myrecipe.core.remote

import com.alysa.myrecipe.core.domain.Favorite.DataFavorite
import com.alysa.myrecipe.core.domain.Favorite.ResponseFavorite
import com.alysa.myrecipe.core.domain.auth.RequestSignIn
import com.alysa.myrecipe.core.domain.auth.RequestSignUp
import com.alysa.myrecipe.core.domain.auth.ResponseSignIn
import com.alysa.myrecipe.core.domain.auth.ResponseSignOut
import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
import com.alysa.myrecipe.core.domain.recipe.ResepResponse
import com.alysa.myrecipe.core.domain.recipe.makanan.ResponseByUnitCategory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServiceSignIn {
    @POST("signIn")
    fun postSignIn(
        @Body requestSignIn: RequestSignIn
    ): Call<ResponseSignIn>
}

interface ApiServiceSignUp {
    @POST("signUp")
    fun postSignUp(
        @Body requestSignUp: RequestSignUp
    ): Call<ResponseSignUp>
}

interface ApiServiceRecipeType {
    @POST("recipe/type")
    fun postRecipeType(
        @Query("unit_id") recipeType: String
    ): Call<ResepResponse>
}

interface ApiServiceRecipeMakanan {
    @POST("recipe")
    fun postRecipeMakanan(
        @Query("unit_id") recipeType: String,
        @Query("category_id") recipeCategory: String
    ): Call<ResponseByUnitCategory>
}

interface ApiServiceRecipeFavorite {
    @POST("add-favorite")
    fun getRecipeDetail(
        @Query("recipe_id") recipeFavorite: String
    ): Call<ResponseFavorite>
}

interface ApiServiceSignOut {
    @POST("signOut")
    fun postSignOut(
        @Header("Authorization") authorization: String
    ): Call<ResponseSignOut>
}

//interface ApiServiceRecipe {
//    @GET("api/v1/")
//    fun getRecipe(
//        @Query("recipe") recipe: String,
//    ): Call<List<ResponseGetRecipe>>
//}
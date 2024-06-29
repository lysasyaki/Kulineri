package com.alysa.myrecipe.core.remote

import com.alysa.myrecipe.core.domain.Favorite.ResponseFavorite
import com.alysa.myrecipe.core.domain.Favorite.delete.ResponseDeleteFav
import com.alysa.myrecipe.core.domain.Favorite.get.ResponseGetFavorite
import com.alysa.myrecipe.core.domain.auth.RequestSignIn
import com.alysa.myrecipe.core.domain.auth.RequestSignUp
import com.alysa.myrecipe.core.domain.auth.ResponseSignOuts
import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
import com.alysa.myrecipe.core.domain.auth.signIn.ResponseSignIn
import com.alysa.myrecipe.core.domain.recipe.ResepResponse
import com.alysa.myrecipe.core.domain.recipe.detail.DetailResponse
import com.alysa.myrecipe.core.domain.recipe.makanan.ResponseByUnitCategory
import com.alysa.myrecipe.core.domain.recipe.upload.ResponseUploadRecipe
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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


interface ApiServiceFavorite {
    @POST("add-favorite")
    fun createFavorite(
        @Header("Authorization") token: String,
        @Query("recipe_id") recipeId: Int
    ): Call<ResponseFavorite>
}

interface ApiServiceSignOut {
    @POST("signOut")
    fun postSignOut(
        @Header("Authorization") authorization: String
    ): Call<ResponseSignOuts>
}

interface ApiServiceDeleteFavorite {
    @DELETE("delete-favorite")
    fun deleteFavorite(
        @Header("Authorization") token: String,
        @Query("recipe_id") recipeId: Int
    ): Call<ResponseDeleteFav>
}

interface ApiServiceUpload {

    @Multipart
    @POST("add-recipe") // Ganti dengan endpoint yang sesuai
    fun postUploadRecipe(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("Ingredient") ingredient: RequestBody,
        @Part("Instruction") instruction: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("unit_id") unitId: RequestBody
    ): Call<ResponseUploadRecipe>
}

interface ApiServiceGetFavorite {
    @GET("get-favorite")
    fun getFavorites(
        @Query("user_id") UserId: Int
    ): Call<ResponseGetFavorite>
}

interface ApiServiceDetail {
    @GET("recipe/{id}")
    fun getRecipeDetail(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Call<DetailResponse>
}
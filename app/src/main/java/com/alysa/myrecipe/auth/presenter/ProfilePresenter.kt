package com.alysa.myrecipe.auth.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.alysa.myrecipe.auth.model.UserPreferences
import com.alysa.myrecipe.core.domain.Favorite.delete.ResponseDeleteFav
import com.alysa.myrecipe.core.domain.Favorite.get.ResponseGetFavorite
import com.alysa.myrecipe.core.domain.recipe.byUser.RecipeByUserResponse
import com.alysa.myrecipe.core.domain.recipe.delete.DataDelete
import com.alysa.myrecipe.core.domain.recipe.delete.DeleteRecipeResponse
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceDeleteFavorite
import com.alysa.myrecipe.core.remote.ApiServiceDeleteRecipe
import com.alysa.myrecipe.core.remote.ApiServiceFavorite
import com.alysa.myrecipe.core.remote.ApiServiceGetFavorite
import com.alysa.myrecipe.core.remote.ApiServiceRecipeByUser
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.core.view.RecipeByUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter (
    private val context: Context,
    private val view: RecipeByUser,
    private val userDataStoreImpl: UserDataStoreImpl
){
    fun getRecipeByUser() {
        try {
            // Mendapatkan user_id dari SharedPreferences
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("id", -1)

            Log.d("ProfilePresenter", "Retrieved userId: $userId")

            if (userId == -1) {
                // Handle error jika userId tidak ditemukan
                view.displayRecipe(ResultState.Error("User ID tidak ditemukan di SharedPreferences"))
                return
            }

            // Mendapatkan token dari UserDataStore
            val token = userDataStoreImpl.getToken()

            token?.let { authToken ->
                // Menginisialisasi ApiService untuk mengambil data favorit
                val apiServiceRecipeByUser = ApiConfig.getApiService(context, "getRecipe") as ApiServiceRecipeByUser

                // Membuat request untuk mengambil favorit berdasarkan user_id dan token
                val call = apiServiceRecipeByUser.getRecipes("Bearer $authToken", userId)
                call.enqueue(object : Callback<RecipeByUserResponse> {
                    override fun onResponse(call: Call<RecipeByUserResponse>, response: Response<RecipeByUserResponse>) {
                        if (response.isSuccessful) {
                            val favoriteResponse = response.body()
                            favoriteResponse?.let {
                                val recipeDataList = it.data
                                Log.d("ProfilePresenter", "Fetched Recipes: $recipeDataList")
                                view.displayRecipe(ResultState.Success(recipeDataList))
                            } ?: run {
                                view.displayRecipe(ResultState.Error("Response body is null"))
                            }
                        } else {
                            view.displayRecipe(ResultState.Error("Gagal mengambil resep favorit: ${response.message()}"))
                        }
                    }

                    override fun onFailure(call: Call<RecipeByUserResponse>, t: Throwable) {
                        Log.e("ProfilePresenter", "Error: ${t.message}")
                        view.displayRecipe(ResultState.Error(t.message.toString()))
                    }
                })
            } ?: run {
                view.displayRecipe(ResultState.Error("Token tidak tersedia"))
            }

        } catch (e: Exception) {
            Log.e("ProfilePresenter", "Error: ${e.message}")
            view.displayRecipe(ResultState.Error(e.message.toString()))
        }
    }

    fun deleteRecipe(id: Int, callback: (Boolean) -> Unit) {
        val token = userDataStoreImpl.getToken()

        token?.let { authToken ->
            val apiServiceDeleteRecipe = ApiConfig.getApiService(context, "deleteRecipe") as? ApiServiceDeleteRecipe
            if (apiServiceDeleteRecipe != null) {
                apiServiceDeleteRecipe.deleteRecipe("Bearer $authToken", id).enqueue(object : Callback<DeleteRecipeResponse> {
                    override fun onResponse(call: Call<DeleteRecipeResponse>, response: Response<DeleteRecipeResponse>) {
                        if (response.isSuccessful) {
                            Log.d("ProfilePresenter", "Delete successful: ${response.body()}")
                            callback(true)
                        } else {
                            Log.e("ProfilePresenter", "Delete failed: ${response.errorBody()?.string()}")
                            callback(false)
                        }
                    }

                    override fun onFailure(call: Call<DeleteRecipeResponse>, t: Throwable) {
                        Log.e("ProfilePresenter", "Delete request failed: ${t.message}")
                        callback(false)
                    }
                })
            } else {
                Log.e("ProfilePresenter", "ApiServiceDeleteRecipe is null")
                callback(false)
            }
        } ?: run {
            Log.e("ProfilePresenter", "Token is null")
            callback(false)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
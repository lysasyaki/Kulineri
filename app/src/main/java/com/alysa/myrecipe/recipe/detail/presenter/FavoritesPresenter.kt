package com.alysa.myrecipe.recipe.detail.presenter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alysa.myrecipe.auth.model.UserPreferences
import com.alysa.myrecipe.core.domain.Favorite.ResponseFavorite
import com.alysa.myrecipe.core.domain.Favorite.delete.ResponseDeleteFav
import com.alysa.myrecipe.core.domain.Favorite.get.DataGet
import com.alysa.myrecipe.core.domain.Favorite.get.ResponseGetFavorite
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceDeleteFavorite
import com.alysa.myrecipe.core.remote.ApiServiceFavorite
import com.alysa.myrecipe.core.remote.ApiServiceRecipeMakanan
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.core.view.RecipeFavorite
import com.alysa.myrecipe.core.view.RecipeMakananView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesPresenter(private val context: Context, private val userPreferences: UserPreferences) {

    private val userDataStoreImpl = UserDataStoreImpl(context)

    fun addFavorite(recipeId: Int, userPreferences: UserPreferences, callback: (Boolean) -> Unit) {
        if (userPreferences.favoriteRecipes.contains(recipeId)) {
            callback(false)
            return
        }
        val token = userDataStoreImpl.getToken()

        token?.let { authToken ->
            val apiServiceFavorite = ApiConfig.getApiService(context, "addFavorite") as ApiServiceFavorite
            val call = apiServiceFavorite.createFavorite("Bearer $authToken", recipeId)

            call.enqueue(object : Callback<ResponseFavorite> {
                override fun onResponse(call: Call<ResponseFavorite>, response: Response<ResponseFavorite>) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (responseData != null) {
                            if (response.code() == 201) {
                                userPreferences.favoriteRecipes.add(recipeId)
                                saveUserPreferences()
                                callback(true)
                            } else if (response.code() == 400) {
                                callback(false)
                            } else {
                                callback(false)
                                Log.e("FavoritePresenter", "Unexpected response code: ${response.code()}")
                            }
                        } else {
                            callback(false)
                            Log.e("FavoritePresenter", "Response body is null")
                        }
                    } else {
                        Log.e("FavoritePresenter", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<ResponseFavorite>, t: Throwable) {
                    Log.e("FavoritePresenter", "Failed to add favorite: ${t.message}", t)
                    callback(false)
                }
            })
        } ?: run {
            callback(false)
            Log.e("FavoritePresenter", "Token is null or empty")
        }
    }

    fun deleteFavorite(recipeId: Int, userPreferences: UserPreferences, callback: (Boolean) -> Unit) {
        val token = userDataStoreImpl.getToken()
        token?.let { authToken ->
            val apiServiceDeleteFavorite = ApiConfig.getApiService(context, "deleteFavorite") as ApiServiceDeleteFavorite
            val call = apiServiceDeleteFavorite.deleteFavorite("Bearer $authToken", recipeId)

            call.enqueue(object : Callback<ResponseDeleteFav> {
                override fun onResponse(call: Call<ResponseDeleteFav>, response: Response<ResponseDeleteFav>) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (responseData != null && responseData.data != null) {
                            userPreferences.favoriteRecipes.remove(recipeId)
                            saveUserPreferences()
                            callback(true)
                            showToast("Delete favorite successful")
                        } else {
                            callback(false)
                            showToast("Failed to delete favorite: empty response")
                            Log.e("FavoritePresenter", "Response body or data is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("FavoritePresenter", "Error: ${response.code()} - $errorBody")
                        callback(false)
                        showToast("Failed to delete favorite")
                    }
                }

                override fun onFailure(call: Call<ResponseDeleteFav>, t: Throwable) {
                    Log.e("FavoritePresenter", "Failed to delete favorite: ${t.message}", t)
                    callback(false)
                    showToast("Failed to delete favorite")
                }
            })
        } ?: run {
            Log.e("FavoritePresenter", "Failed to get token")
            callback(false)
            showToast("Failed to delete favorite")
        }
    }

    private fun saveUserPreferences() {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("user_preferences", Gson().toJson(userPreferences))
            apply()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
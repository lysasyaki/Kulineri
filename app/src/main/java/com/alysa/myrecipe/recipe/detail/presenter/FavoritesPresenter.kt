package com.alysa.myrecipe.recipe.detail.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.alysa.myrecipe.core.domain.Favorite.ResponseFavorite
import com.alysa.myrecipe.core.domain.Favorite.delete.ResponseDeleteFav
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceDeleteFavorite
import com.alysa.myrecipe.core.remote.ApiServiceFavorite
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesPresenter (
    private val context: Context, private val userDataStoreImpl: UserDataStoreImpl
){

    fun addFavorite(
        recipeId: Int,
        callback: (Boolean) -> Unit)
    {
        val tokenManager = userDataStoreImpl.getToken()
        tokenManager.let { token ->
            val apiServiceFavorite = ApiConfig.getApiService(context, "addFavorite") as ApiServiceFavorite
            val call = apiServiceFavorite.createFavorite("Bearer $token", recipeId)
            call.enqueue(object : Callback<ResponseFavorite> {
                override fun onResponse(
                    call: Call<ResponseFavorite>,
                    response: Response<ResponseFavorite>
                ) {
                    if (response.isSuccessful) {
                        userDataStoreImpl.saveFavoriteRecipeId(recipeId)
                        callback(true)
                        showToast("Save to favorite successful")
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
        }
    }

    fun deleteFavorite(recipeId: Int, callback: (Boolean) -> Unit) {
        val tokenManager = userDataStoreImpl.getToken()
        tokenManager?.let { token ->
            val apiServiceFavorite = ApiConfig.getApiService(context, "deleteFavorite") as ApiServiceDeleteFavorite
            val call = apiServiceFavorite.deleteFavorite("Bearer $token", recipeId)
            call.enqueue(object : Callback<ResponseDeleteFav> {
                override fun onResponse(call: Call<ResponseDeleteFav>, response: Response<ResponseDeleteFav>) {
                    if (response.isSuccessful) {
                        userDataStoreImpl.removeFavoriteRecipeId(recipeId)
                        callback(true)
                        showToast("Delete favorite successful")
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

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
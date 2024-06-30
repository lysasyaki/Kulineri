package com.alysa.myrecipe.recipe.detail.presenter

import android.content.Context
import android.util.Log
import com.alysa.myrecipe.core.domain.Favorite.ResponseFavorite
import com.alysa.myrecipe.core.domain.Favorite.get.ResponseGetFavorite
import com.alysa.myrecipe.core.remote.ApiServiceGetFavorite
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.view.RecipeFavorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetFavPresenter (
    private val context: Context,
    private val view: RecipeFavorite,
    private val apiServiceGetFavorite: ApiServiceGetFavorite
){

    fun getFavorite(context: Context) {
        try {
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("id", -1)

            Log.d("GetFavPresenter", "Retrieved userId: $userId")

            if (userId == -1) {
                // Handle error jika userId tidak ditemukan
                view.displayFavorite(ResultState.Error("User ID tidak ditemukan di SharedPreferences"))
                return
            }

            val call = apiServiceGetFavorite.getFavorites(userId)
            call.enqueue(object : Callback<ResponseGetFavorite> {
                override fun onResponse(call: Call<ResponseGetFavorite>, response: Response<ResponseGetFavorite>) {
                    if (response.isSuccessful) {
                        val favoriteResponse = response.body()
                        favoriteResponse?.let {
                            val favoriteDataList = it.data
                            favoriteDataList.let {
                                view.displayFavorite(ResultState.Success(favoriteDataList))
                            }
                        } ?: run {
                            view.displayFavorite(ResultState.Error("Response body is null"))
                        }
                    } else {
                        view.displayFavorite(ResultState.Error("Failed to fetch favorite recipes: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<ResponseGetFavorite>, t: Throwable) {
                    Log.e("GetFavPresenter", "Error: ${t.message}")
                    view.displayFavorite(ResultState.Error(t.message.toString()))
                }
            })

        } catch (e: Exception) {
            Log.e("GetFavPresenter", "Error: ${e.message}")
            view.displayFavorite(ResultState.Error(e.message.toString()))
        }
    }
}
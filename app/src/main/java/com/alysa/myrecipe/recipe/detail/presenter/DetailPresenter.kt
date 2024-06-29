package com.alysa.myrecipe.recipe.detail.presenter

import android.content.Context
import android.util.Log
import com.alysa.myrecipe.core.domain.recipe.detail.DataDetail
import com.alysa.myrecipe.core.domain.recipe.detail.DetailResponse
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.domain.recipe.makanan.ResponseByUnitCategory
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceDetail
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.core.view.RecipeDetailView
import com.alysa.myrecipe.core.view.RecipeMakananView
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailPresenter (private val view: RecipeDetailView, private val userDataStoreImpl: UserDataStoreImpl, private val context: Context) {

    var currentDataItem: DataDetail? = null

    fun getDetailRecipe(id: Int) {
        val token = userDataStoreImpl.getToken()
        token?.let { tokenValue ->
            val apiServiceDetail = ApiConfig.getApiService(context, "detailRecipe") as ApiServiceDetail
            val call = apiServiceDetail.getRecipeDetail("Bearer $tokenValue", id)
            call.enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val data = responseBody.data
                            if (data != null) {
                                view.displayDetail(ResultState.Success(data))
                            } else {
                                view.displayDetail(ResultState.Error("Data not found"))
                            }
                        } else {
                            view.displayDetail(ResultState.Error("Empty response"))
                        }
                    } else {
                        Log.e("DetailPresenter", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                        view.displayDetail(ResultState.Error("Error fetching data from API"))
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.e("DetailPresenter", "Failed to fetch recipe details: ${t.message}", t)
                    view.displayDetail(ResultState.Error("Failed to fetch recipe details"))
                }
            })
        }
    }
}
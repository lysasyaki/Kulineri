package com.alysa.myrecipe.Makanan.presenter

import android.util.Log
import com.alysa.myrecipe.core.domain.recipe.ResepResponse
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.domain.recipe.makanan.ResponseByUnitCategory
import com.alysa.myrecipe.core.remote.ApiServiceRecipeMakanan
import com.alysa.myrecipe.core.remote.ApiServiceRecipeType
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.view.RecipeMakananView
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class RecipeMakananPresenter (
    private val apiServiceRecipeMakanan: ApiServiceRecipeMakanan,
    private val view : RecipeMakananView
){
        fun getRecipeMakanan(recipeType: String, recipeCategory: String) {
            try {
                val call = apiServiceRecipeMakanan.postRecipeMakanan(recipeType, recipeCategory)
                call.enqueue(object : Callback<ResponseByUnitCategory> {
                    override fun onResponse(call: Call<ResponseByUnitCategory>, response: Response<ResponseByUnitCategory>) {
                        if (response.isSuccessful) {
                            val makananResponse = response.body()
                            makananResponse?.let {
                                val food = it.data
                                food?.let {
                                    view.displayRecipe(ResultState.Success(food))
                                }
                            } ?: run {
                                view.displayRecipe(ResultState.Error("Response body is null"))
                            }
                        } else {
                            view.displayRecipe(ResultState.Error("Failed to fetch recipes: ${response.message()}"))
                        }
                    }

                    override fun onFailure(call: Call<ResponseByUnitCategory>, t: Throwable) {
                        Log.e("Product", "Error: ${t.message}")
                        view.displayRecipe(ResultState.Error(t.message.toString()))
                    }
                })

            } catch (e: Exception) {
                Log.e("Product", "Error: ${e.message}")
                view.displayRecipe(ResultState.Error(e.message.toString()))
            }
        }
    }
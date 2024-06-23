package com.alysa.myrecipe.home.presenter

import android.util.Log
import com.alysa.myrecipe.core.domain.recipe.Data
import com.alysa.myrecipe.core.domain.recipe.ResepResponse
import com.alysa.myrecipe.core.remote.ApiServiceRecipeType
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.view.RecipeTypeView
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections.addAll

class RecipeTypePresenter (
    private val apiServiceRecipeType : ApiServiceRecipeType,
//    private val apiServiceRecipeType2 : ApiServiceRecipeType2,
    private val view : RecipeTypeView
){

    fun getRecipeByType (
        recipeType: String
    ) {
        try {
            val call = apiServiceRecipeType.postRecipeType(recipeType)

            call.enqueue(object : Callback<ResepResponse>{
                override fun onResponse(call: Call<ResepResponse>, response: Response<ResepResponse>) {
                    if (response.isSuccessful) {
                        val recipeResponse = response.body()
                        recipeResponse?.let {
                            val recipes = it.data
                            recipes?.let {
                                for (recipe in recipes) {
                                    saveProductToRealm(recipe)
                                }
                                view.displayRecipe(ResultState.Success(recipes))
                            }
                        } ?: run {
                            view.displayRecipe(ResultState.Error("Response body is null"))
                        }
                    } else {
                        view.displayRecipe(ResultState.Error("Failed to fetch recipes: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<ResepResponse>, t: Throwable) {
                    Log.e("Product", "Error: ${t.message}")
                    view.displayRecipe(ResultState.Error(t.message.toString()))
                }
            })

        } catch (e: Exception) {
            Log.e("Product", "Error: ${e.message}")
            view.displayRecipe(ResultState.Error(e.message.toString()))
        }
    }

//    fun getRecipeByType(recipeType: String) {
//        try {
//            val call = apiServiceRecipeType.postRecipeType(recipeType)
//
//            call.enqueue(object : Callback<ResepResponse> {
//                override fun onResponse(call: Call<ResepResponse>, response: Response<ResepResponse>) {
//                    if (response.isSuccessful) {
//                        val recipeResponse = response.body()
//                        recipeResponse?.let {
//                            val recipes = it.data
//                            view.displayRecipe(ResultState.Success(recipes ?: emptyList()))
//                        } ?: run {
//                            view.displayRecipe(ResultState.Error("Response body is null"))
//                        }
//                    } else {
//                        view.displayRecipe(ResultState.Error("Failed to fetch recipes: ${response.message()}"))
//                    }
//                }
//
//                override fun onFailure(call: Call<ResepResponse>, t: Throwable) {
//                    Log.e("Product", "Error: ${t.message}")
//                    view.displayRecipe(ResultState.Error(t.message ?: "Unknown error"))
//                }
//            })
//
//        } catch (e: Exception) {
//            Log.e("Product", "Error: ${e.message}")
//            view.displayRecipe(ResultState.Error(e.message ?: "Unknown error"))
//        }
//    }

//    fun getRecipeByType2 (
//        recipeType: String
//    ) {
//        try {
//            val call = apiServiceRecipeType2.postRecipeType(recipeType)
//
//            call.enqueue(object : Callback<List<ResponseRecipeByTypeId>> {
//                override fun onResponse(call: Call<List<ResponseRecipeByTypeId>>, response: Response<List<ResponseRecipeByTypeId>>) {
//                    if (response.isSuccessful) {
//                        val resep = response.body()
//                        resep?.let {
//                            for (recipe in resep) {
//                                saveProductToRealm(recipe)
//                            }
//                        }
//                        view.displayRecipe(ResultState.Success(resep as List<ResponseRecipeByTypeId>))
//                        Log.d("Product", "Response: $response")
//                    } else {
//                        Log.e("Product", "Error: ${response.message()}")
//                        view.displayRecipe(ResultState.Error(response.message()))
//                    }
//                }
//
//                override fun onFailure(call: Call<List<ResponseRecipeByTypeId>>, t: Throwable) {
//                    Log.e("Product", "Error: ${t.message}")
//                    view.displayRecipe(ResultState.Error(t.message.toString()))
//                }
//            })
//
//        } catch (e: Exception) {
//            Log.e("Product", "Error: ${e.message}")
//            view.displayRecipe(ResultState.Error(e.message.toString()))
//        }
//    }

    fun saveProductToRealm(dataItem: Data) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync(
            { backgroundRealm ->
                backgroundRealm.insertOrUpdate(dataItem)
            },
            {
                realm.close()
            },
            { error ->
                error.printStackTrace()
                realm.close()
            }
        )
    }

    fun getDataByIdFromRealm(uniqueId: Int): Data? {
        val realm = Realm.getDefaultInstance()
        return realm.where(Data::class.java).equalTo("id", uniqueId).findFirst()
    }

    fun getProductFromRealm() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Data::class.java).findAll()

        if (result.isNotEmpty()) {
            val items = mutableListOf<Data>().apply {
                addAll(realm.copyFromRealm(result))
            }
            view.displayRecipe(ResultState.Success(items))
        } else {
            view.displayRecipe(ResultState.Error(""))
        }

        realm.close()
    }


}
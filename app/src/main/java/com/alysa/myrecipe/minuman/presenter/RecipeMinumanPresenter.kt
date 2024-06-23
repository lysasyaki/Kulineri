package com.alysa.myrecipe.minuman.presenter

import android.util.Log
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.domain.recipe.makanan.ResponseByUnitCategory
import com.alysa.myrecipe.core.remote.ApiServiceRecipeMakanan
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.view.RecipeMakananView
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class RecipeMinumanPresenter (
    private val apiServiceRecipeMakanan: ApiServiceRecipeMakanan,
    private val view : RecipeMakananView
){

    fun getRecipeMinuman (
        recipeType: String,
        recipeCategory: String
    ) {
        try {
            val call = apiServiceRecipeMakanan.postRecipeMakanan(recipeType, recipeCategory)

            call.enqueue(object : Callback<ResponseByUnitCategory> {
                override fun onResponse(call: Call<ResponseByUnitCategory>, response: Response<ResponseByUnitCategory>) {
                    if (response.isSuccessful) {
                        val makananResponse = response.body()
                        makananResponse?.let {
                            val food = it.data
                            food?.let {
                                for (food in food) {
                                    saveResepToRealm(food)
                                }
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
    fun saveResepToRealm(data: DataItem) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync(
            { backgroundRealm ->
                backgroundRealm.insertOrUpdate(data)
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

    fun getDataByIdFromRealm(uniqueId: Int): DataItem? {
        val realm = Realm.getDefaultInstance()
        return realm.where(DataItem::class.java).equalTo("id", uniqueId).findFirst()
    }

    fun getProductFromRealm() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(DataItem::class.java).findAll()

        if (result.isNotEmpty()) {
            val items = mutableListOf<DataItem>().apply {
                addAll(realm.copyFromRealm(result))
            }
            view.displayRecipe(ResultState.Success(items))
        } else {
            view.displayRecipe(ResultState.Error(""))
        }

        realm.close()
    }

    fun retrieveProductTagFromRealm() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(DataItem::class.java).findAll()

        if (result.isNotEmpty()) {
            val items = RealmList<DataItem>().apply {
                Collections.addAll(realm.copyFromRealm(result))
            }
            view.displayRecipe(ResultState.Success(items))
        } else {
            view.displayRecipe(ResultState.Error("No data in Realm"))
        }

        realm.close()
    }
}
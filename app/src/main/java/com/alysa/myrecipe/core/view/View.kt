package com.alysa.myrecipe.core.view

import com.alysa.myrecipe.core.domain.Favorite.DataFavorite
import com.alysa.myrecipe.core.domain.Favorite.get.DataGet
import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
import com.alysa.myrecipe.core.domain.auth.signIn.ResponseSignIn
import com.alysa.myrecipe.core.domain.recipe.Data
import com.alysa.myrecipe.core.domain.recipe.category.ResponseCategory
import com.alysa.myrecipe.core.domain.recipe.category.ResponseType
import com.alysa.myrecipe.core.domain.recipe.detail.DataDetail
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.utils.ResultState
import io.realm.RealmList

sealed interface viewSignUp{
    fun displaySignUp(result: ResultState<List<ResponseSignUp>>)
}

sealed interface viewSignIn{
    fun displaySignIn(result: ResultState<List<ResponseSignIn>>)
}

interface RecipeTypeView {
    fun displayRecipe(result: ResultState<List<Data>?>)
}

interface RecipeMakananView {
    fun displayRecipe(result: ResultState<List<DataItem>?>)
}

interface SignOutView {
    fun onSignOutSuccess(Message: String)
    fun onSignOutError(errorMessage: String)
}

interface RecipeFavorite {
    fun displayFavorite(result: ResultState<List<DataGet>?>)
}

interface RecipeDetailView {
    fun displayDetail(result: ResultState<DataDetail>)
}

interface RecipeAddFavorite {
    fun displayRecipe(result: ResultState<DataFavorite>)
}

interface UploadRecipeContract {
    interface View {
        fun showCategories(categories: List<ResponseCategory>)
        fun showTypes(types: List<ResponseType>)
        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadCategories()
//        fun loadTypes()
    }
}
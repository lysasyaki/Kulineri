package com.alysa.myrecipe.core.view

import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
import com.alysa.myrecipe.core.domain.auth.signIn.ResponseSignIn
import com.alysa.myrecipe.core.domain.recipe.Data
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.utils.ResultState

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
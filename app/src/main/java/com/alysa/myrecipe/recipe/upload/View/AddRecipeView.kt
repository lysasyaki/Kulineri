package com.alysa.myrecipe.recipe.upload.View

import com.alysa.myrecipe.core.domain.recipe.upload.DataUpload

interface AddRecipeView {
    fun showAddRecipeSuccessMessage(message: String?, data: DataUpload?)
    fun showAddRecipeErrorMessage(message: String?)
}
package com.alysa.myrecipe.core.domain.recipe.byUser

import com.google.gson.annotations.SerializedName

data class RecipeByUserResponse(

	@field:SerializedName("data")
	val data: List<DataByUser>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
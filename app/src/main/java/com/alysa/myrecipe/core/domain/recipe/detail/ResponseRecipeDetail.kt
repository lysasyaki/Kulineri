package com.alysa.myrecipe.core.domain.recipe.detail

import com.google.gson.annotations.SerializedName

data class ResponseRecipeDetail(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)
package com.alysa.myrecipe.core.domain.recipe.delete

import com.google.gson.annotations.SerializedName

data class DeleteRecipeResponse(

	@field:SerializedName("data")
	val data: DataDelete? = null,

	@field:SerializedName("message")
	val message: String? = null
)
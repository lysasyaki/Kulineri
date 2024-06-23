package com.alysa.myrecipe.core.domain.recipe.makanan

import com.google.gson.annotations.SerializedName

data class ResponseByUnitCategory(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
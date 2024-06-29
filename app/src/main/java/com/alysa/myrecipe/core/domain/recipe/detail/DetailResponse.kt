package com.alysa.myrecipe.core.domain.recipe.detail

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("data")
	var data: DataDetail? = null,

	@field:SerializedName("message")
	var message: String? = null
)

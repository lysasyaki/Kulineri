package com.alysa.myrecipe.core.domain.Favorite

import com.google.gson.annotations.SerializedName

data class ResponseFavorite(

	@field:SerializedName("data")
	var data: DataFavorite? = null,

	@field:SerializedName("message")
	var message: String? = null
)
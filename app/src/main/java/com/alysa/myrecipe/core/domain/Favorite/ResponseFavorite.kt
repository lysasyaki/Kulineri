package com.alysa.myrecipe.core.domain.Favorite

import com.google.gson.annotations.SerializedName

data class ResponseFavorite(

	@field:SerializedName("data")
	val data: DataFavorite? = null,

	@field:SerializedName("message")
	val message: String? = null
)
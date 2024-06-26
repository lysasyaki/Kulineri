package com.alysa.myrecipe.core.domain.Favorite.delete

import com.google.gson.annotations.SerializedName

data class ResponseDeleteFav(

	@field:SerializedName("data")
	val data: DataDeleteFav? = null,

	@field:SerializedName("message")
	val message: String? = null
)

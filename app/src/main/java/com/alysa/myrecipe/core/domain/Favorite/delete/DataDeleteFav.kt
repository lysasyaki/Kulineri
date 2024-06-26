package com.alysa.myrecipe.core.domain.Favorite.delete

import com.google.gson.annotations.SerializedName

data class DataDeleteFav(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("recipeId")
	val recipeId: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("updateAt")
	val updatedAt: String? = null
)

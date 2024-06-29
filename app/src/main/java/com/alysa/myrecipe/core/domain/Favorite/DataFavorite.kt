package com.alysa.myrecipe.core.domain.Favorite

import com.google.gson.annotations.SerializedName

data class DataFavorite(

	@field:SerializedName("createdAt")
	var createdAt: String? = null,

	@field:SerializedName("recipe_id")
	var recipeId: Int? = null,

	@field:SerializedName("user_id")
	var userId: Int? = null,

	@field:SerializedName("id")
	var id: Int? = null,

	@field:SerializedName("updatedAt")
	var updatedAt: String? = null
)
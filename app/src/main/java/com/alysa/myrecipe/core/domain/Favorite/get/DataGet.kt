package com.alysa.myrecipe.core.domain.Favorite.get

import com.google.gson.annotations.SerializedName

data class DataGet(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("recipe_id")
	val recipeId: Int? = null,

	@field:SerializedName("user_Id")
	val userId: Int? = null,

	@field:SerializedName("recipe")
	val recipe: Recipe? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

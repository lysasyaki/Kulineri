package com.alysa.myrecipe.core.domain.recipe.detail

import com.google.gson.annotations.SerializedName

data class DataDetail(

	@field:SerializedName("image")
	var image: List<String>? = null,

	@field:SerializedName("createdAt")
	var createdAt: String? = null,

	@field:SerializedName("category_id")
	var categoryId: Int? = null,

	@field:SerializedName("userId")
	var userId: Int? = null,

	@field:SerializedName("Ingredient")
	var ingredient: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("id")
	var id: Int? = null,

	@field:SerializedName("Instruction")
	var instruction: String? = null,

	@field:SerializedName("unit_id")
	var unitId: Int? = null,

	@field:SerializedName("user")
	var user: User? = null,

	@field:SerializedName("updateAt")
	var updatedAt: String? = null
)

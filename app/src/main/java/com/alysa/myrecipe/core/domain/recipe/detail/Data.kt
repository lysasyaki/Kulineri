package com.alysa.myrecipe.core.domain.recipe.detail

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("image")
	val image: List<String?>? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("Ingredient")
	val ingredient: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("Instruction")
	val instruction: String? = null,

	@field:SerializedName("unit_id")
	val unitId: Int? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
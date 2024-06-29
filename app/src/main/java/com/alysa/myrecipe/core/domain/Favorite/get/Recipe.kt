package com.alysa.myrecipe.core.domain.Favorite.get

import com.google.gson.annotations.SerializedName

data class Recipe(

	@field:SerializedName("image")
	val image: List<String>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null
)

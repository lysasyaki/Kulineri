package com.alysa.myrecipe.core.domain.recipe.detail

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("name")
	var name: String? = null
)

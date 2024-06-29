package com.alysa.myrecipe.core.domain.recipe.upload

import com.google.gson.annotations.SerializedName

data class ResponseUploadRecipe(

	@field:SerializedName("dataUpload")
	var data: DataUpload? = null,

	@field:SerializedName("message")
	var message: String? = null
)

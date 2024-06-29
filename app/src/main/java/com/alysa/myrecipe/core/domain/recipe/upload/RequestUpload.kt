package com.alysa.myrecipe.core.domain.recipe.upload

import com.google.gson.annotations.SerializedName

data class RequestUpload(
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("category_id") var categoryId: String,
    @SerializedName("unit_id") var unitId: String,
    @SerializedName("ingredient") var ingredient: String,
    @SerializedName("instruction") var instruction: String,
    @SerializedName("image") var image: List<String>?
)
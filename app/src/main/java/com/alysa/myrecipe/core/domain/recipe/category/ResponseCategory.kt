package com.alysa.myrecipe.core.domain.recipe.category

import com.google.gson.annotations.SerializedName

data class ResponseCategory(

    @field:SerializedName("data")
    val data: List<DataCategory?>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

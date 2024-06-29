package com.alysa.myrecipe.core.domain.recipe.category

import com.google.gson.annotations.SerializedName

data class ResponseType(

    @field:SerializedName("data")
    val data: List<DataType?>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

package com.alysa.myrecipe.core.domain.Favorite.get

import com.google.gson.annotations.SerializedName

data class ResponseGetFavorite(

    @field:SerializedName("data")
    val data: List<DataGet>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

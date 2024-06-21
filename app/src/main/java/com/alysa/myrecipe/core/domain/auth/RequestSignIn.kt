package com.alysa.myrecipe.core.domain.auth

import com.google.gson.annotations.SerializedName

data class RequestSignIn(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String
)

package com.alysa.myrecipe.core.domain.auth

import com.google.gson.annotations.SerializedName

data class RequestSignUp(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("name") var name: String,
    @SerializedName("age") var age: String
)

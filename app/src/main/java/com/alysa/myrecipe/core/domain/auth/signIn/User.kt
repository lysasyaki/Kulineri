package com.alysa.myrecipe.core.domain.auth.signIn

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)
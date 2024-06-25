package com.alysa.myrecipe.core.domain.auth.signIn

import com.google.gson.annotations.SerializedName

data class ResponseSignIn(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)
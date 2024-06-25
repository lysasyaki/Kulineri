package com.alysa.myrecipe.core.domain.auth

import com.google.gson.annotations.SerializedName

data class ResponseSignOuts(

	@field:SerializedName("auth")
	val auth: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: Any? = null
)
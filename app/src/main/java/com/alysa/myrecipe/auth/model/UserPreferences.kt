package com.alysa.myrecipe.auth.model

data class UserPreferences(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val password: String? = null,
    val age: String? = null,
    val token: String? = null,
    val refreshToken: String? = null,
)
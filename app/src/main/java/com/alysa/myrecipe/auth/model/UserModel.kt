package com.alysa.myrecipe.auth.model

data class UserModel(
    var user_id: Int = 0,
    var name: String? = null,
    var username: String? = null,
    var password: String? = null,
    var confirm: String? = null
) {
    companion object {

        fun isPasswordValid(password: String, confirm: String): Boolean {
            val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+\$).{6,}\$")
            return passwordPattern.matches(password) && password == confirm
        }
    }
}
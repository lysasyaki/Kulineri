//package com.alysa.myrecipe.auth.model
//
//import android.content.Context
//
//class AuthManager(private val context: Context) {
//
//    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
//
//    fun saveAuthToken(token: String) {
//        sharedPreferences.edit().putString("auth_token", token).apply()
//    }
//
//    fun getToken(): String? {
//        return sharedPreferences.getString("auth_token", null)
//    }
//
//    fun isTokenSaved(): Boolean {
//        return sharedPreferences.getString("auth_token", null) != null
//    }
//}

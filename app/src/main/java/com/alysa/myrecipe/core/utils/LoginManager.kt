package com.alysa.myrecipe.core.utils

import android.content.Context
import io.realm.Realm

object LoginManager {
    private const val PREF_NAME = "login_pref"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private val realm: Realm by lazy {Realm.getDefaultInstance()}

    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

//    fun getToken(): String? {
//        val userToken = realm.where(UserToken::class.java).equalTo("id", "user_token").findFirst()
//        return userToken?.token
//    }

    fun close() {
        realm.close()
    }
}
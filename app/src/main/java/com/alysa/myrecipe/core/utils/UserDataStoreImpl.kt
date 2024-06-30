package com.alysa.myrecipe.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.alysa.myrecipe.auth.model.RecipePreferances
import com.alysa.myrecipe.auth.model.UserPreferences
import com.google.gson.Gson

class UserDataStoreImpl(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String, refreshToken: String?, id: Int, username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putString("refreshToken", refreshToken)
        editor.putInt("id", id)
        editor.putString("username", username)
//        editor.putString("age", age)
        editor.apply()
        Log.d("UserDataStoreImpl", "Token saved: $token")
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun getUserId(): Int {
        val userId = sharedPreferences.getInt("id", -1)
        Log.d("UserDataStoreImpl", "Retrieved userId: $userId")
        return userId
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }

    fun clearToken() {
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.remove("refreshToken")
        editor.remove("id")
        editor.remove("username")
        editor.apply()
        Log.d("UserDataStoreImpl", "Token cleared")
    }

    fun addUser(id: Int, name: String, username: String, age: String) {
        val editor = sharedPreferences.edit()
        editor.putInt("id", id)
        editor.putString("name", name)
        editor.putString("username", username)
        editor.putString("age", age)
        editor.apply()
        Log.d("UserDataStoreImpl", "User added: $username")
    }

    fun getUser(): UserPreferences {
        val id = sharedPreferences.getInt("id", -1)
        val name = sharedPreferences.getString("name", "")
        val username = sharedPreferences.getString("username", "")
        val age = sharedPreferences.getString("age", "")

        return UserPreferences(id, name ?: "", username ?: "", age ?: "")
    }

    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.remove("id")
        editor.remove("name")
        editor.remove("username")
        editor.remove("age")
        editor.apply()
        Log.d("UserDataStoreImpl", "User cleared")
    }

    fun saveFavoriteStatus(recipeId: Int, isFavorite: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("favorite_$recipeId", isFavorite)
        editor.apply()
        Log.d("UserDataStoreImpl", "Favorite status saved: Recipe $recipeId isFavorite: $isFavorite")
    }

}
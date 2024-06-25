//package com.alysa.myrecipe.auth.model
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//interface AuthRepository {
//    suspend fun getUser(): UserPreferences
//    suspend fun saveUserLogin(user: UserPreferences)
//    suspend fun clearUserLogin()
//    suspend fun saveUserToken(token: String)
//    suspend fun refreshToken(refreshToken: String)
//}
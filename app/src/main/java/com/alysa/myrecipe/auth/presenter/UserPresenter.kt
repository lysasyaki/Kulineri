package com.alysa.myrecipe.auth.presenter

import android.content.Context
import android.util.Log
import com.alysa.myrecipe.auth.model.UserPreferences
import com.alysa.myrecipe.core.domain.auth.RequestSignIn
import com.alysa.myrecipe.core.domain.auth.RequestSignUp
import com.alysa.myrecipe.core.domain.auth.ResponseSignOuts
import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
import com.alysa.myrecipe.core.domain.auth.signIn.ResponseSignIn
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceSignIn
import com.alysa.myrecipe.core.remote.ApiServiceSignOut
import com.alysa.myrecipe.core.remote.ApiServiceSignUp
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPresenter(private val context: Context, private val userDataStoreImpl: UserDataStoreImpl){

    init {
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }


    fun login(username: String, password: String, callback: (Boolean) -> Unit) {
        val requestSignIn = RequestSignIn(username, password)

        val apiServiceSignIn = ApiConfig.getApiService(context, "signIn") as ApiServiceSignIn
        val call = apiServiceSignIn.postSignIn(requestSignIn)

        call.enqueue(object : Callback<ResponseSignIn> {
            override fun onResponse(call: Call<ResponseSignIn>, response: Response<ResponseSignIn>) {
                if (response.isSuccessful) {
                    val signInResponse = response.body()
                    Log.d("UserPresenter", "Response body: $signInResponse")
                    if (signInResponse != null) {
                        val message = signInResponse.message
                        val user = signInResponse.user
                        if (user != null) {
                            val token = user.token
                            val refreshToken = user.refreshToken
                            val id = user.id
                            if (!token.isNullOrBlank() && !refreshToken.isNullOrBlank() && id !=null) {
                                // Save token and refreshToken
                                userDataStoreImpl.saveToken(token, refreshToken, id)
                                Log.d("UserPresenter", "Token saved: $token")
                                callback(true)
                            } else {
                                Log.e("UserPresenter", "Token or refreshToken is null or empty")
                                callback(false)
                            }
                        } else {
                            Log.e("UserPresenter", "User object in response is null")
                            callback(false)
                        }
                    } else {
                        Log.e("UserPresenter", "Response object is null")
                        callback(false)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UserPresenter", "Error: ${response.code()} - $errorBody")
                    callback(false)
                }
            }
            override fun onFailure(call: Call<ResponseSignIn>, t: Throwable) {
                Log.e("UserPresenter", "Failed to execute login request: ${t.message}", t)
                callback(false)
            }
        })
    }

    fun signup(username: String, password: String, name: String, age: String, callback: (Boolean) -> Unit) {
        val requestSignUp = RequestSignUp(username, password, name, age)

        val apiServiceSignUp = ApiConfig.getApiService(context, "signUp") as ApiServiceSignUp
        val call = apiServiceSignUp.postSignUp(requestSignUp)

        call.enqueue(object : Callback<ResponseSignUp> {
            override fun onResponse(call: Call<ResponseSignUp>, response: Response<ResponseSignUp>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null) {
                    val userResponses = response.body()
                    val userPreferences = UserPreferences(
                        id = userResponses?.id,
                        name = userResponses?.name ?: "",
                        username = userResponses?.username ?: "",
                        age = userResponses?.age ?: "",
                    )
                    // Save user data to UserDataStoreImpl
                    userPreferences.id?.let {
                        userDataStoreImpl.addUser(
                            it,
                            userPreferences.name ?: "",
                            userPreferences.username ?: "",
                            userPreferences.age ?: ""// Assuming you have password in user preferences
                        )
                    }
                    Log.d("UserPresenter", "User added: ${userPreferences.username}")
                    callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UserPresenter", "Error: ${response.code()} - $errorBody")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<ResponseSignUp>, t: Throwable) {
                Log.e("UserPresenter", "Failed to execute sign-up request: ${t.message}", t)
                callback(false)
            }
        })
    }

    fun signOut(callback: (Boolean) -> Unit) {
        val token = userDataStoreImpl.getToken()

        if (token != null) {
            // Lakukan proses sign out di sini (misalnya panggil API signout)
            val apiServiceSignOut = ApiConfig.getApiService(context, "signOut") as ApiServiceSignOut
            val call = apiServiceSignOut.postSignOut("Bearer $token")

            call.enqueue(object : Callback<ResponseSignOuts> {
                override fun onResponse(call: Call<ResponseSignOuts>, response: Response<ResponseSignOuts>) {
                    if (response.isSuccessful) {
                        // Clear token and refresh token locally
                        userDataStoreImpl.clearUser()
                        callback(true)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UserPresenter", "Error: ${response.code()} - $errorBody")
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<ResponseSignOuts>, t: Throwable) {
                    Log.e("UserPresenter", "Failed to execute sign out request: ${t.message}", t)
                    callback(false)
                }
            })
        } else {
            // Token tidak tersedia
            callback(false)
        }
    }
}
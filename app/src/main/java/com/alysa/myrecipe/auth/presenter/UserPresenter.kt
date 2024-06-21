package com.alysa.myrecipe.auth.presenter

import android.content.Context
import android.util.Log
import com.alysa.myrecipe.core.domain.auth.RequestSignIn
import com.alysa.myrecipe.core.domain.auth.RequestSignUp
import com.alysa.myrecipe.core.domain.auth.ResponseSignIn
import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceSignIn
import com.alysa.myrecipe.core.remote.ApiServiceSignUp
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPresenter(private val context: Context) {

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
                    val userResponse = response.body()
                    if (userResponse != null) {
                        saveUserToRealm(userResponse)
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
                        saveUserToRealm(userResponse)
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

    private fun <T : RealmModel> saveUserToRealm(user: T) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ realm ->
            realm.copyToRealmOrUpdate(user)
        }, {
            // Transaksi Realm berhasil selesai
            realm.close()
        }, { error ->
            // Tangani kesalahan jika ada
            Log.e("UserPresenter", "Failed to save user to Realm: ${error.message}")
            realm.close()
        })
    }

}

package com.alysa.myrecipe.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.alysa.myrecipe.auth.model.UserPreferences

class UserDataStoreImpl(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String, refreshToken: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putString("refreshToken", refreshToken)
        editor.apply()
        Log.d("UserDataStoreImpl", "Token saved: $token")
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }

    fun clearToken() {
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.remove("refreshToken")
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

        return UserPreferences(id, name, username, age)
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
}

//class UserDataStoreImpl (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//    companion object {
//        private const val DATABASE_VERSION = 1
//        private const val DATABASE_NAME = "root"
//        private const val TABLE_USERS = "users"
//        private const val KEY_ID = "id"
//        private const val KEY_NAME = "name"
//        private const val KEY_USERNAME = "username"
//        private const val KEY_AGE = "age"
//        private const val KEY_TOKEN = "token"
//        private const val KEY_REFRESH_TOKEN = "refreshtoken"
//        private const val CREATE_TABLE_USERS =
//            ("CREATE TABLE $TABLE_USERS ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_USERNAME TEXT, $KEY_AGE INTEGER, $KEY_TOKEN STRING, $KEY_REFRESH_TOKEN STRING)")
//    }
//
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL(CREATE_TABLE_USERS)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
//        onCreate(db)
//    }
//
//    fun addUser(user: UserPreferences) {
//        val db = this.writableDatabase
//        val values = ContentValues()
//        values.put(KEY_NAME, user.name)
//        values.put(KEY_USERNAME, user.username)
//        values.put(KEY_AGE, user.age)
//        values.put(KEY_TOKEN, user.token)
//        values.put(KEY_REFRESH_TOKEN, user.refreshToken)
//        db.insert(TABLE_USERS, null, values)
//        db.close()
//    }
//
////    fun saveToken(user: UserPreferences) {
////        val db = this.writableDatabase
////        val values = ContentValues()
////        values.put(KEY_TOKEN, user.token)
////        values.put(KEY_REFRESH_TOKEN, user.refreshToken)
////        db.insert(TABLE_USERS, null, values)
////        db.close()
////    }
//
//    fun saveToken(user: UserPreferences) {
//        val editor = sharedPreferences.edit()
//        editor.putString("token", user.token)
//        editor.putString("refreshToken", user.refreshToken)
//        editor.apply()
//        Log.d("UserDataStoreImpl", "Token saved: ${user.token}")
//    }
//
//    fun getToken(): String? {
//        return sharedPreferences.getString("token", null)
//    }
//
//
//        fun getAllUsers(): List<UserPreferences> {
//            val userList = mutableListOf<UserPreferences>()
//            val selectQuery = "SELECT * FROM $TABLE_USERS"
//            val db = this.readableDatabase
//            val cursor = db.rawQuery(selectQuery, null)
//
//            userList.clear()
//
//            while (cursor.moveToNext()) {
//                val idIndex = cursor.getColumnIndex(KEY_ID)
//                val idName = cursor.getColumnIndex(KEY_NAME)
//                val idUsername = cursor.getColumnIndex(KEY_USERNAME)
//                val idAge = cursor.getColumnIndex(KEY_AGE)
//                val idToken = cursor.getColumnIndex(KEY_TOKEN)
//                val idRefreshToken = cursor.getColumnIndex(KEY_REFRESH_TOKEN)
//
//                if (idIndex >= 0 && idName >= 0 && idUsername >= 0 && idAge >= 0 && idToken >= 0 && idRefreshToken >= 0) {
//                    val id = cursor.getInt(idIndex)
//                    val name = cursor.getString(idName)
//                    val username = cursor.getString(idUsername)
//                    val age = cursor.getString(idAge)
//                    val token = cursor.getString(idToken)
//                    val refreshToken = cursor.getString(idRefreshToken)
//
//                    val user = UserPreferences(id, name, age, username, token, refreshToken)
//                    userList.add(user)
//                }
//            }
//            cursor.close()
//            return userList
//        }
//}
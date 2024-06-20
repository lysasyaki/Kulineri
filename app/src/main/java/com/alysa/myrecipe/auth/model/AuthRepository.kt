package com.alysa.myrecipe.auth.model

import com.alysa.myrecipe.core.utils.dbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val dbHelper: dbHelper = dbHelper()

    suspend fun login(username: String, password: String): UserModel? {
        return withContext(Dispatchers.IO) {
            dbHelper.login(username, password)
        }
    }

    suspend fun signup(users: UserModel): Boolean {
        return withContext(Dispatchers.IO) {
            if (UserModel.isPasswordValid(users.password ?: "", users.confirm ?: "")) {
                dbHelper.signup(users)
            } else {
                false
            }
        }
    }
}
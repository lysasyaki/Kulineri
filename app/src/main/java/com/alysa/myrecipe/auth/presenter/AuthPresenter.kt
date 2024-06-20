//package com.alysa.myrecipe.auth.presenter
//
//import com.alysa.myrecipe.auth.model.AuthRepository
//import com.alysa.myrecipe.auth.model.UserModel
//import com.alysa.myrecipe.auth.view.AuthView
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class AuthPresenter(private val view: AuthView) {
//    private val repository = AuthRepository()
//
//    fun signup(user: UserModel) {
//        CoroutineScope(Dispatchers.Main).launch {
//            val result = withContext(Dispatchers.IO) {
//                repository.signup(user)
//            }
//            if (result) {
//                view.onSignupSuccess()
//            } else {
//                view.onSignupError("Signup gagal. Silakan periksa input Anda.")
//            }
//        }
//    }
//
//    // Fungsi login jika dibutuhkan
//    fun login(username: String, password: String) {
//        CoroutineScope(Dispatchers.Main).launch {
//            val user = withContext(Dispatchers.IO) {
//                repository.login(username, password)
//            }
//            if (user != null) {
//                view.onLoginSuccess(user)
//            } else {
//                view.onLoginError("Login gagal. Silakan periksa kredensial Anda.")
//            }
//        }
//    }
//}
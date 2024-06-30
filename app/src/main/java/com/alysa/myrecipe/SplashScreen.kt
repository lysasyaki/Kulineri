package com.alysa.myrecipe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.alysa.myrecipe.auth.presentation.SignInActivity
import com.alysa.myrecipe.core.utils.LoginManager
import com.alysa.myrecipe.core.utils.RealmManager
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen: AppCompatActivity() {

private lateinit var splashScope: CoroutineScope

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Realm.init(this)
    RealmManager.initRealm()

    splashScope = CoroutineScope(Dispatchers.Main)
    splashScope.launch {
        delaySplashScreen()
    }
}

private suspend fun delaySplashScreen() {
    delay(1000) // Tampilkan splash screen selama 1 detik
    navigateToAppropriateScreen()
}

//    private fun navigateToAppropriateScreen() {
//        val intent = Intent(this, SignInActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

    private fun navigateToAppropriateScreen() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        val intent = if (token.isNullOrEmpty()) {
            Intent(this, SignInActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java) // Ganti dengan aktivitas utama Anda
        }
        startActivity(intent)
        finish()
    }


override fun onDestroy() {
    super.onDestroy()
    splashScope.cancel()
}
}
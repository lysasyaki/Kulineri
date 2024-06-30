package com.alysa.myrecipe.auth.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.alysa.myrecipe.MainActivity
import com.alysa.myrecipe.R
import com.alysa.myrecipe.recipe.FavoriteActivity
import com.alysa.myrecipe.recipe.ResepkuActivity
import kotlinx.coroutines.MainScope

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val out = findViewById<FrameLayout>(R.id.btn_keluar)
        val back = findViewById<ImageView>(R.id.iv_back)
        val resep = findViewById<FrameLayout>(R.id.btn_resep)
        val fav = findViewById<FrameLayout>(R.id.btn_resepku)
        val name = findViewById<TextView>(R.id.tv_name_profile)

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Guest")

        // Tampilkan username di TextView
        name.text = username

        out.setOnClickListener {
            val intent = Intent(this,ConfirmationActivity::class.java)
            startActivity(intent)
            finish()
        }

        fav.setOnClickListener {
            val intent = Intent(this,ResepkuActivity::class.java)
            startActivity(intent)
            finish()
        }

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        resep.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
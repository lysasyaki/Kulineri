package com.alysa.myrecipe.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.alysa.myrecipe.MainActivity
import com.alysa.myrecipe.R
import kotlinx.coroutines.MainScope

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val out = findViewById<FrameLayout>(R.id.btn_keluar)
        val back = findViewById<ImageView>(R.id.iv_back)

        out.setOnClickListener {
            val intent = Intent(this,ConfirmationActivity::class.java)
            startActivity(intent)
            finish()
        }

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.alysa.myrecipe.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.alysa.myrecipe.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val out = findViewById<FrameLayout>(R.id.btn_keluar)

        out.setOnClickListener {
            val intent = Intent(this,ConfirmationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
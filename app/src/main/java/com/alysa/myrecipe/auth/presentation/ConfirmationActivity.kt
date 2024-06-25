package com.alysa.myrecipe.auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.alysa.myrecipe.R
import com.alysa.myrecipe.auth.presenter.UserPresenter
import com.alysa.myrecipe.core.utils.LoginManager

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var signOutPresenter: UserPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val out = findViewById<Button>(R.id.btn_yes)
        val no = findViewById<Button>(R.id.btn_no)

        no.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        signOutPresenter = UserPresenter(this)

//        out.setOnClickListener {
//            signOutPresenter.signOut { success, errorMessage ->
//                if (success) {
//                    // Clear token dan arahkan ke SignInActivity
//                    LoginManager.clearLogin(this)
//                    startActivity(Intent(this, SignInActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this, "Failed to sign out: $errorMessage", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }
}
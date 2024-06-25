package com.alysa.myrecipe.auth.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.alysa.myrecipe.R
import com.alysa.myrecipe.auth.presenter.UserPresenter
import com.alysa.myrecipe.core.utils.LoginManager
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.core.view.SignOutView

class ConfirmationActivity : AppCompatActivity(), SignOutView {

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
        val userDataStoreImpl = UserDataStoreImpl(this)
        signOutPresenter = UserPresenter(this, userDataStoreImpl)


        out.setOnClickListener {
            signOutPresenter.signOut { success ->
                if (success) {
                    onSignOutSuccess("Success SignOut")

                    val intent = Intent(this@ConfirmationActivity, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    onSignOutError("Failed to sign out")
                }
            }
        }
    }
    override fun onSignOutSuccess(message: String) {
        Toast.makeText(this, "Sign Out Success: $message", Toast.LENGTH_SHORT).show()
    }

    override fun onSignOutError(errorMessage: String) {
        // Implement your logic when sign out encounters an error
        // Example: Show an error message to the user
        Toast.makeText(this, "Sign Out Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }
}
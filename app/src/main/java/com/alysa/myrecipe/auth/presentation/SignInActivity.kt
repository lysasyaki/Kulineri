package com.alysa.myrecipe.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alysa.myrecipe.MainActivity
import com.alysa.myrecipe.R
import com.alysa.myrecipe.auth.presenter.UserPresenter
import com.alysa.myrecipe.core.domain.auth.ResponseSignIn
import com.alysa.myrecipe.core.utils.LoginManager
import com.google.android.material.textfield.TextInputLayout

class SignInActivity : AppCompatActivity() {

    private lateinit var presenter: UserPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        presenter = UserPresenter(this) // Initialize your presenter here

        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val btnLogin = findViewById<FrameLayout>(R.id.btn_login)
        val btnRegister = findViewById<Button>(R.id.btn_regis)

        findViewById<TextView>(R.id.tv_name).visibility = View.GONE
        findViewById<EditText>(R.id.authNameEditText).visibility = View.GONE
        findViewById<TextView>(R.id.tv_age).visibility = View.GONE
        findViewById<EditText>(R.id.authAgeEditText).visibility = View.GONE
        findViewById<TextView>(R.id.tv_confirm_password).visibility = View.GONE
        findViewById<TextView>(R.id.authConfirmPasswordEditText).visibility = View.GONE
        findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout).visibility = View.GONE
        findViewById<TextInputLayout>(R.id.authNameTextLayout).visibility = View.GONE
        findViewById<TextInputLayout>(R.id.authAgeTextLayout).visibility = View.GONE

        val authUsernameTextLayout = findViewById<TextInputLayout>(R.id.authUsernameTextLayout)
        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)

        authUsernameTextLayout.helperText = ""
        authPasswordTextLayout.helperText = ""

        authPasswordTextLayout.setEndIconOnClickListener {
            val editText = authPasswordTextLayout.editText
            editText?.let {
                if (editText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                editText.setSelection(editText.text.length)
            }
        }

        val usnWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val username = s.toString()
                if (username.isBlank()) {
                    authUsernameTextLayout.helperText = "Required*"
                    authUsernameTextLayout.error = null
                } else {
                    authUsernameTextLayout.helperText = null
                    authUsernameTextLayout.error = null
                }
            }
        }

        usernameEditText.addTextChangedListener(usnWatcher)

        btnLogin.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                presenter.login(username, password) { isSuccess ->
                    if (isSuccess) {

                        val intent = Intent(this, MainActivity::class.java) // Ganti dengan kelas utama setelah login
                        startActivity(intent)
                        LoginManager.saveLoginStatus(this, true)
                        finish()
                    } else {
                        onLoginError("Login failed")
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_LONG).show()
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

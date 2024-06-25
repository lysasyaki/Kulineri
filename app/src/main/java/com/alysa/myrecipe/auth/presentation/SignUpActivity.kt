package com.alysa.myrecipe.auth.presentation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.alysa.myrecipe.R
import com.google.android.material.textfield.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Spinner
import android.widget.TextView
import com.alysa.myrecipe.auth.presenter.UserPresenter
import com.alysa.myrecipe.core.domain.auth.RequestSignUp
import com.alysa.myrecipe.core.utils.LoginManager
import com.alysa.myrecipe.core.utils.UserDataStoreImpl

class SignUpActivity : AppCompatActivity(){

    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val userDataStoreImpl = UserDataStoreImpl(this)
        presenter = UserPresenter(this, userDataStoreImpl) // Initialize your presenter here

        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
        val btnRegister = findViewById<FrameLayout>(R.id.btn_daftar)
        val btnLogin = findViewById<Button>(R.id.btn_masuk)
        val nameEditText = findViewById<EditText>(R.id.authNameEditText)
        val ageEditText = findViewById<EditText>(R.id.authAgeEditText)
        val passConfEditText = findViewById<TextView>(R.id.authConfirmPasswordEditText)

        val authUsernameTextLayout = findViewById<TextInputLayout>(R.id.authUsernameTextLayout)
        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)
        val authPassCornTextLayout = findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout)
        val authNameTextLayout = findViewById<TextInputLayout>(R.id.authNameTextLayout)
        val authAgeTextLayout = findViewById<TextInputLayout>(R.id.authAgeTextLayout)


        authUsernameTextLayout.helperText = ""
        authPasswordTextLayout.helperText = ""
        authPassCornTextLayout.helperText = ""
        authNameTextLayout.helperText = ""
        authAgeTextLayout.helperText = ""

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

        authPassCornTextLayout.setEndIconOnClickListener {
            val editText = authPassCornTextLayout.editText
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
                val name = s.toString()
                if (name.isBlank()) {
                    authNameTextLayout.helperText = "Required*"
                    authNameTextLayout.error = null
                } else {
                    authNameTextLayout.helperText = null
                    authNameTextLayout.error = null
                }
                val age = s.toString()
                if (age.isBlank()) {
                    authAgeTextLayout.helperText = "Required*"
                    authAgeTextLayout.error = null
                } else {
                    authAgeTextLayout.helperText = null
                    authAgeTextLayout.error = null
                }
            }
        }

        usernameEditText.addTextChangedListener(usnWatcher)
        nameEditText.addTextChangedListener(usnWatcher)
        ageEditText.addTextChangedListener(usnWatcher)
        passwordEditText.addTextChangedListener(usnWatcher)
        passConfEditText.addTextChangedListener(usnWatcher)

        val passwordWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString()

                if (pass.isBlank()) {
                    authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
                    authPasswordTextLayout.helperText = "at least input 8 character*"
                    authPasswordTextLayout.error = null // Menghapus pesan kesalahan jika ada
                } else {
                    authPasswordTextLayout.error = null
                    authPasswordTextLayout.helperText = "Good Password"
                    authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN))
                }
            }
        }

        passwordEditText.addTextChangedListener(passwordWatcher)

        passConfEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val passcon = s.toString()
                val pass= passwordEditText.text.toString()

                if (passcon.isBlank()) {
                    authPassCornTextLayout.helperText = "Required*"
                    authPassCornTextLayout.error = null // Menghapus pesan kesalahan jika ada
                } else if (pass != passcon) {
                    authPassCornTextLayout.error = "Password do not match"
                    authPassCornTextLayout.helperText = null // Menghapus pesan bantuan jika password tidak valid
                } else {
                    authPassCornTextLayout.error = null
                    authPassCornTextLayout.helperText = null // Menghapus pesan bantuan jika password valid
                }
            }
        })

        btnRegister.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank() && name.isNotBlank() && age.isNotBlank()) {
                presenter.signup(username, password, name, age) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this, "Registration successful. Please proceed to login.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignInActivity::class.java) // Ganti dengan kelas utama setelah login
                        startActivity(intent)
                        finish()
                    } else {
                        onSignUpError("SignUp failed")
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_LONG).show()
            }
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onSignUpError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

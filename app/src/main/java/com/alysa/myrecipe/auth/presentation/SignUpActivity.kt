//package com.alysa.myrecipe.auth.presentation
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.FrameLayout
//import android.widget.Toast
//import com.alysa.myrecipe.R
//import com.alysa.myrecipe.auth.model.UserModel
//import com.alysa.myrecipe.auth.presenter.AuthPresenter
//import com.alysa.myrecipe.auth.view.AuthView
//import com.google.android.material.textfield.TextInputLayout
//import android.text.Editable
//import android.text.TextWatcher
//import android.content.res.ColorStateList
//import android.graphics.Color
//
//class SignUpActivity : AppCompatActivity(), AuthView {
//    private lateinit var presenter: AuthPresenter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
//
//        presenter = AuthPresenter(this)
//
//        val nameField = findViewById<EditText>(R.id.authNameEditText)
//        val usernameField = findViewById<EditText>(R.id.authUserNameEditText)
//        val passwordField = findViewById<EditText>(R.id.authPasswordEditText)
//        val confirmPasswordField = findViewById<EditText>(R.id.authConfirmPasswordEditText)
//        val btnSignin = findViewById<Button>(R.id.btn_masuk)
//        val btnSignup = findViewById<FrameLayout>(R.id.btn_daftar)
//
//        val authNameTextLayout = findViewById<TextInputLayout>(R.id.authNameTextLayout)
//        val authUsernameTextLayout = findViewById<TextInputLayout>(R.id.authUsernameTextLayout)
//        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)
//        val authConfirmPasswordTextLayout = findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout)
//
//        // Menyembunyikan pesan helper untuk Name dan Username saat awal
//        authNameTextLayout.helperText = ""
//        authUsernameTextLayout.helperText = ""
//        authPasswordTextLayout.helperText = ""
//        authConfirmPasswordTextLayout.helperText = ""
//
//        val nameWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val name = s.toString()
//                if (name.isBlank()) {
//                    authNameTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
//                    authNameTextLayout.helperText = "Required*"
//                    authNameTextLayout.error = null // Menghapus pesan kesalahan jika ada
//                } else {
//                    authNameTextLayout.helperText = null
//                    authNameTextLayout.error = null
//                }
//            }
//        }
//
//        nameField.addTextChangedListener(nameWatcher)
//
//        val usernameWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val username = s.toString()
//                if (username.isBlank()) {
//                    authUsernameTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
//                    authUsernameTextLayout.helperText = "Required*"
//                    authUsernameTextLayout.error = null // Menghapus pesan kesalahan jika ada
//                } else {
//                    authUsernameTextLayout.helperText = null
//                    authUsernameTextLayout.error = null
//                }
//            }
//        }
//
//        usernameField.addTextChangedListener(usernameWatcher)
//
//        val passwordWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val password = s.toString()
//                val userModel = UserModel()
//
//                if (password.isBlank()) {
//                    authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
//                    authPasswordTextLayout.helperText = "Required*"
//                    authPasswordTextLayout.error = null // Menghapus pesan kesalahan jika ada
//                } else if (!UserModel.isPasswordValid(password, password)) {
//                    authPasswordTextLayout.error = "Password harus 6 karakter dan 1 angka"
//                    authPasswordTextLayout.helperText = null // Menghapus pesan bantuan jika password tidak valid
//                } else {
//                    authPasswordTextLayout.error = null
//                    authPasswordTextLayout.helperText = "Good Password"
//                    authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN))
//                }
//            }
//        }
//
//        passwordField.addTextChangedListener(passwordWatcher)
//
//        confirmPasswordField.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val confirm = s.toString()
//                val password = passwordField.text.toString()
//
//                if (confirm.isBlank()) {
//                    authConfirmPasswordTextLayout.helperText = "Required*"
//                    authConfirmPasswordTextLayout.error = null // Menghapus pesan kesalahan jika ada
//                } else if (confirm != password) {
//                    authConfirmPasswordTextLayout.error = "Password tidak sesuai"
//                    authConfirmPasswordTextLayout.helperText = null // Menghapus pesan bantuan jika password tidak valid
//                } else {
//                    authConfirmPasswordTextLayout.error = null
//                    authConfirmPasswordTextLayout.helperText = null // Menghapus pesan bantuan jika password valid
//                }
//            }
//        })
//
//        btnSignup.setOnClickListener {
//            val name = nameField.text.toString()
//            val username = usernameField.text.toString()
//            val password = passwordField.text.toString()
//            val confirm = confirmPasswordField.text.toString()
//
//            if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank() && confirm == password) {
//                val user = UserModel(name = name, username = username, password = password, confirm = confirm)
//                presenter.signup(user)
//            } else {
//                if (name.isBlank()) {
//                    authNameTextLayout.helperText = "Required*"
//                }
//                if (username.isBlank()) {
//                    authUsernameTextLayout.helperText = "Required*"
//                }
//                if (password.isBlank()) {
//                    authPasswordTextLayout.helperText = "Required*"
//                }
//                if (confirm.isBlank()) {
//                    authConfirmPasswordTextLayout.helperText = "Required*"
//                }
//                Toast.makeText(this, "Harap lengkapi semua bagian yang kosong dengan benar", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        btnSignin.setOnClickListener {
//            val intent = Intent(this, SignInActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    override fun onLoginSuccess(user: UserModel) {
//        // No implementation needed for registration view
//    }
//
//    override fun onLoginError(message: String) {
//        // No implementation needed for registration view
//    }
//
//    override fun onSignupSuccess() {
//        Toast.makeText(this, "Berhasil Mendaftar", Toast.LENGTH_LONG).show()
//        val intent = Intent(this, SignInActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    override fun onSignupError(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//}

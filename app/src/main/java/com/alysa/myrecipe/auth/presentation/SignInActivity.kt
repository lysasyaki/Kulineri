//package com.alysa.myrecipe.auth.presentation
//
//import android.content.Intent
//import android.content.res.ColorStateList
//import android.graphics.Color
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.text.method.HideReturnsTransformationMethod
//import android.text.method.PasswordTransformationMethod
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.FrameLayout
//import android.widget.TextView
//import android.widget.Toast
//import com.alysa.myrecipe.R
//import com.alysa.myrecipe.auth.model.UserModel
//import com.alysa.myrecipe.auth.presenter.AuthPresenter
//import com.alysa.myrecipe.auth.view.AuthView
//import com.google.android.material.textfield.TextInputLayout
//
//class SignInActivity : AppCompatActivity(), AuthView {
//    private lateinit var presenter: AuthPresenter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//
//        presenter = AuthPresenter(this)
//
//        val usernameEditText = findViewById<EditText>(R.id.authUserNameEditText)
//        val passwordEditText = findViewById<EditText>(R.id.authPasswordEditText)
//        val btnLogin = findViewById<FrameLayout>(R.id.btn_login)
//        val btnRegister = findViewById<Button>(R.id.btn_regis)
//
//        findViewById<TextView>(R.id.tv_name).visibility = View.GONE
//        findViewById<EditText>(R.id.authNameEditText).visibility = View.GONE
//        findViewById<TextView>(R.id.tv_confirm_password).visibility = View.GONE
//        findViewById<TextView>(R.id.authConfirmPasswordEditText).visibility = View.GONE
//        findViewById<TextInputLayout>(R.id.authConfirmPasswordTextLayout).visibility = View.GONE
//        findViewById<TextInputLayout>(R.id.authNameTextLayout).visibility = View.GONE
//
//        val authUsernameTextLayout = findViewById<TextInputLayout>(R.id.authUsernameTextLayout)
//        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.authPasswordTextLayout)
//
//        authUsernameTextLayout.helperText = ""
//        authPasswordTextLayout.helperText = ""
//
//        authPasswordTextLayout.setEndIconOnClickListener {
//            // Mengubah visibilitas teks password sesuai status saat ini
//            val editText = authPasswordTextLayout.editText
//            editText?.let {
//                if (editText.transformationMethod == PasswordTransformationMethod.getInstance()) {
//                    // Jika teks password disembunyikan, tampilkan
//                    editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
//                } else {
//                    // Jika teks password ditampilkan, sembunyikan
//                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
//                }
//                // Memastikan teks kembali ke posisi terakhir
//                editText.setSelection(editText.text.length)
//            }
//        }
//
//        val usnWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val username = s.toString()
//
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
//        usernameEditText.addTextChangedListener(usnWatcher)
//
//        val passwordWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val password = s.toString()
//
//                if (password.isBlank()) {
//                    authPasswordTextLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
//                    authPasswordTextLayout.helperText = "Required*"
//                    authPasswordTextLayout.error = null // Menghapus pesan kesalahan jika ada
//                } else {
//                    authPasswordTextLayout.helperText = null
//                    authPasswordTextLayout.error = null
//                }
//            }
//        }
//
//        passwordEditText.addTextChangedListener(passwordWatcher)
//
//        btnLogin.setOnClickListener {
//            val username = usernameEditText.text.toString()
//            val password = passwordEditText.text.toString()
//
//            if (username.isBlank()) {
//                authUsernameTextLayout.helperText = "Required*"
//            }
//            if (password.isBlank()) {
//                authPasswordTextLayout.helperText = "Required*"
//            }
//
//            if (username.isNotEmpty() && password.isNotEmpty()) {
//                presenter.login(username, password)
//            } else {
//                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        btnRegister.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    override fun onLoginSuccess(user: UserModel) {
//        Toast.makeText(this, "Berhasil Login", Toast.LENGTH_LONG).show()
//        // val intent = Intent(this, DashboardActivity::class.java)
//        // intent.putExtra("user", user)
//        // startActivity(intent)
//    }
//
//    override fun onLoginError(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//
//    override fun onSignupSuccess() {
//        Toast.makeText(this, "Berhasil Mendaftar", Toast.LENGTH_LONG).show()
//    }
//
//    override fun onSignupError(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//}

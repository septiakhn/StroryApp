package com.example.stroryapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.stroryapp.data.Result
import com.example.stroryapp.data.pref.UserModel
import com.example.stroryapp.databinding.ActivityLoginBinding
import com.example.stroryapp.databinding.ActivitySignupBinding
import com.example.stroryapp.viewModel.LoginViewModel
import com.example.stroryapp.viewModel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.postLogin(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            result.data.loginResult?.let { loginResult ->
                                viewModel.saveSession(
                                    UserModel(
                                        email,
                                        result.data.loginResult?.token!!,
                                        result.data.loginResult.name!!,
                                        true
                                    )
                                )
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Gagal login: ${result.error}", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()

            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                setPositiveButton("Lanjut") { _, _ ->
                    finish()
                }
                create()
                show()
            }
        }
    }
}



package com.example.stroryapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.stroryapp.data.pref.UserModel
import com.example.stroryapp.databinding.ActivityLoginBinding
import com.example.stroryapp.viewModel.LoginViewModel
import com.example.stroryapp.viewModel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = getViewModel(this)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.isButtonEnabled.observe(this) { isEnabled ->
            binding.loginButton.isEnabled = isEnabled
        }

        viewModel.isLoadingLogin.observe(this) { isLoading ->
            binding.apply {
                if (isLoading) {
                    loginButton.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } else {
                    loginButton.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.errorResponse.observe(this) { error ->
            when (error.second) {
                "Invalid password" -> binding.passwordEditTextLayout.error = "Invalid password"
                "User not found" -> binding.emailEditTextLayout.error = "User not found"
            }
        }

        viewModel.isLoginSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: finish LoginActivity to prevent going back to it
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            emailEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.emailValidation(text.toString())
            }

            passwordEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.passwordValidation(text.toString())
            }

            loginButton.setOnClickListener {
                viewModel.postLogin()
            }
        }
    }

    private fun getViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }
}


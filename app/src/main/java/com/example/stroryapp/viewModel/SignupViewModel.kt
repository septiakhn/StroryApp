package com.example.stroryapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.stroryapp.data.UserRepository

class SignupViewModel (private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = repository.signup(name, email, password)
}
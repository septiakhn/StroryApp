package com.example.stroryapp.viewModel

import android.app.Application
import android.content.Context
import android.telecom.Call
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stroryapp.R
import com.example.stroryapp.data.UserRepository
import com.example.stroryapp.data.pref.UserModel
import com.example.stroryapp.response.LoginResponse
import com.example.stroryapp.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun postLogin (email : String, password : String) = repository.login(email, password)
}

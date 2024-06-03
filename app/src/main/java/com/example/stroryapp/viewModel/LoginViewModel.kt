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
import com.example.stroryapp.response.LoginResponse
import com.example.stroryapp.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {
    private val _isEmailValid = MutableLiveData<Boolean>()
    private val _isPasswordValid = MutableLiveData<Boolean>()

    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin

    private val _errorResponse = MutableLiveData<Pair<Boolean, String>>()
    val errorResponse: LiveData<Pair<Boolean, String>> = _errorResponse

    private val _isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(_isEmailValid) { value = it && (_isPasswordValid.value ?: false) }
        addSource(_isPasswordValid) { value = it && (_isEmailValid.value ?: false) }
    }
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _isLoginSuccess = MutableLiveData<Boolean>()
    val isLoginSuccess: LiveData<Boolean> = _isLoginSuccess

    private val applicationContext: Context = application.applicationContext

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private var email = ""
    private var password = ""

    fun postLogin() {
        _isLoadingLogin.value = true

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().login(email, password)
                }
                _isLoadingLogin.value = false
                if (response.loginResult != null) {
                    Log.d(TAG, response.loginResult.token.toString())
                    _isLoginSuccess.value = true
                } else {
                    _errorResponse.value = Pair(true, "Unknown error")
                }
            } catch (e: Exception) {
                _isLoadingLogin.value = false
                _errorResponse.value = Pair(true, applicationContext.getString(R.string.unknown_error))
            }
        }
    }

    fun emailValidation(email: String) {
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        this.email = email
    }

    fun passwordValidation(password: String) {
        if (password.isNotEmpty() && password.length >= 8) {
            _isPasswordValid.value = true
        } else {
            _isPasswordValid.value = false
        }
        this.password = password
    }

//    private fun getErrorResponse(errorBody: String): ErrorResponse {
//         Implement the parsing of errorBody to ErrorResponse based on your API response structure
//         For example, using Gson:
//        return Gson().fromJson(errorBody, ErrorResponse::class.java)
//    }
}

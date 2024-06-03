package com.example.stroryapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.stroryapp.data.UserRepository

class DetailViewModel (private val repository: UserRepository) : ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
    fun getDetail(token: String, id: String) = repository.getDetail(token,id)
}
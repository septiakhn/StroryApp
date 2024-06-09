package com.example.stroryapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.stroryapp.data.UserRepository
import com.example.stroryapp.data.pref.UserModel
import com.example.stroryapp.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel (private val repository: UserRepository) : ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
//    fun loadStory(token: String): LiveData<Result<List<ListStoryItem>>> {
//        val resultLiveData = MutableLiveData<Result<List<ListStoryItem>>>()
//
//        viewModelScope.launch {
//            repository.getStory(token).collect { result ->
//                resultLiveData.value = result
//            }
//        }
//
//        return resultLiveData
//    }
    fun getStory(token: String) = repository.getStory(token)
//    fun getStory(token: String): LiveData<Result<List<ListStoryItem>>> = repository.getStory(token)
    fun getLogout () {
        viewModelScope.launch {
            repository.logout()
        }
    }

//    fun getStory (token: String) : LiveData<PagingData<ListStoryItem>> = repository.getPaging(token)
//    fun logout() {
//        viewModelScope.launch {
//            repository.logout()
//        }
//    }
}
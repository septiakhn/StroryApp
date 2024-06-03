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

class MainViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application)  {
    private val _storyData = MutableLiveData<List<UserModel>>()
    val storyData: LiveData<List<UserModel>> = _storyData

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun getStory() {
        viewModelScope.launch {
            try {
                val result = repository.getData() // Fetch data from repository
                _storyData.value = result
            } catch (e: Exception) {
                // Handle error
                Log.e("MainViewModel", "Error loading data", e)
            }
        }
    }

//    fun getStory (token: String) : LiveData<PagingData<ListStoryItem>> = repository.getPaging(token)
//    fun logout() {
//        viewModelScope.launch {
//            repository.logout()
//        }
//    }
}
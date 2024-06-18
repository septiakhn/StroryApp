package com.example.stroryapp.viewModel

import android.health.connect.datatypes.ExerciseRoute
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.stroryapp.data.Result
import com.example.stroryapp.data.UserRepository
import com.example.stroryapp.response.StoryResponse

class MapViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _location = MutableLiveData<ExerciseRoute.Location>()
    val location: LiveData<ExerciseRoute.Location> get() = _location

    fun setLocation(location: ExerciseRoute.Location) {
        _location.value = location
    }

    fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> {
        return userRepository.getStoriesWithLocation().asLiveData()
    }
}

package com.example.stroryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.example.stroryapp.data.pref.UserModel
import com.example.stroryapp.data.pref.UserPreference
import com.example.stroryapp.response.ListStoryItem
import com.example.stroryapp.response.StoryResponse
import com.example.stroryapp.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun signup(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }
//    fun getStory(token: String): LiveData<Result<List<ListStoryItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getStories("Bearer $token")
//            if (response.isSuccessful) {
//                val storyResponse = response.body()
//                if (storyResponse?.error == false) {
//                    val storyList = storyResponse.listStory ?: emptyList()
//                    emit(Result.Success(storyList))
//                } else {
//                    emit(Result.Error("Error: ${storyResponse?.message ?: "Unknown error"}"))
//                }
//            } else {
//                emit(Result.Error("Error: ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            emit(Result.Error("Error: ${e.message}"))
//        }
//    }
//

//    fun getStory(token: String): LiveData<Result<List<ListStoryItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getStories("Bearer $token")
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            emit(Result.Error("${e.message}"))
//        }
//    }
    //1
    fun getStory(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories("Bearer $token")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun getDetail(token: String, id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetail("Bearer $token", id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun uploadImage(token: String, imageFile: File, description: String, lat: String, lon: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val requestLat = lat.toRequestBody("text/plain".toMediaType())
        val requestLong = lon.toRequestBody("text/plain".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.getUpload("Bearer $token", multipartBody, requestBody, requestLat, requestLong)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }
    fun uploadImage(token: String, imageFile: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.getUpload("Bearer $token", multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, userPreference)
        }.also { instance = it }
    }
}
package com.example.stroryapp.retrofit

import com.example.stroryapp.response.DetailResponse
import com.example.stroryapp.response.LoginResponse
import com.example.stroryapp.response.RegisterResponse
import com.example.stroryapp.response.StoryResponse
import com.example.stroryapp.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun getUpload(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latLng: RequestBody,
        @Part("lon") long: RequestBody
    ): UploadResponse

    @Multipart
    @POST("stories")
    suspend fun getUpload(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ):UploadResponse

}
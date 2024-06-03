package com.example.stroryapp.data

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}
//sealed class Result <out R> private constructor() {
//    data class Success<out T>(val data: T) : Result<T>()
//    data class Error(val error: String) : Result<Nothing>()
//    object Loading : Result<Nothing>()
//}

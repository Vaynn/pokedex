package com.example.pokedex.network

import okhttp3.internal.http2.ErrorCode

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String = "", val errorCode: Int? = null) : ApiResponse<Nothing>()

}
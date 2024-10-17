package com.example.pokedex.network

import retrofit2.HttpException
import java.io.IOException

object ErrorMessages {
    // Network-related errors
    const val NETWORK_ERROR =
        "It seems like your internet connection is lost in the tall grass. Try reconnecting!"

    // HTTP-related errors
    const val HTTP_ERROR = "Team Rocket's causing trouble on the server. We'll have it fixed soon!"

    // General errors
    const val GENERAL_ERROR = "Jigglypuff put the server to sleep. Please try again later!"

    fun <T> handleException(e: Exception): ApiResponse<T> {
        return when (e) {
            is HttpException -> {
                ApiResponse.Error(HTTP_ERROR)
            }
            is IOException -> {
                ApiResponse.Error(NETWORK_ERROR)
            }
            else -> {
                ApiResponse.Error(GENERAL_ERROR)
            }
        }
    }
}



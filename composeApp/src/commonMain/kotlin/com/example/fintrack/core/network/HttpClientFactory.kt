package com.example.fintrack.core.network

class HttpClient

object HttpClientFactory {
    fun create(enableLogging: Boolean = true): HttpClient = HttpClient()
}

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

suspend fun <T> safeApiCall(block: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(block())
    } catch (e: Exception) {
        NetworkResult.Error(e.message ?: "Unknown error")
    }
}

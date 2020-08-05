package com.zeronfinity.cpfy.model.network

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}

data class ErrorResponse(
    val error_description: String, // This is the translated error shown to the user directly from the API
    val causes: Map<String, String> = emptyMap() // This is for errors on specific field on a form
)

package com.core.network

import kotlinx.serialization.Serializable

@Serializable
object NoContent

@Serializable
data class ApiError(val code: Int, val message: String)

class NoInternetException() : Exception("There is no internet connection")

class ApiException(apiError: ApiError) : Exception("Error code: ${apiError.code} with message: ${apiError.message}")
package com.firebase.auth

expect class ResendTokenHandle

interface OtpListener {
    fun onSent(verificationId: String, resendTokenHandle: ResendTokenHandle)
    fun onFailed(exception: Exception)
    fun onInstantVerified(code: String?)
}

enum class AuthStatus {
    SIGNED_IN, SIGNED_OUT
}
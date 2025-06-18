package com.firebase.auth


interface Auth {
    suspend fun sendOtp(phoneNumber: String, otpListener: OtpListener, resendToken: ResendTokenHandle?)
    suspend fun verifyOtp(verificationId: String, otpCode: String): Result<String>
    suspend fun getAuthToken(refresh: Boolean = true): Result<String>

    fun statusListener(onChanged: (AuthStatus) -> Unit)
    suspend fun signOut()
}

internal class AuthImpl: Auth {
    override suspend fun sendOtp(
        phoneNumber: String,
        otpListener: OtpListener,
        resendToken: ResendTokenHandle?
    ) {
        sendOtpCode(phoneNumber, otpListener, resendToken)
    }

    override suspend fun verifyOtp(
        verificationId: String,
        otpCode: String
    ): Result<String> {
        return verifyOtpCode(verificationId, otpCode)
    }

    override suspend fun getAuthToken(refresh: Boolean): Result<String> {
        return getFirebaseIdToken(refresh)
    }

    override fun statusListener(onChanged: (AuthStatus) -> Unit) {
        firebaseAuthStateListener(onChanged)
    }

    override suspend fun signOut() {
        firebaseAuthSignOut()
    }
}

expect suspend fun sendOtpCode(
    phoneNumber: String,
    otpListener: OtpListener,
    resendToken: ResendTokenHandle?
)

expect suspend fun verifyOtpCode(verificationId: String, otpCode: String): Result<String>
expect suspend fun getFirebaseIdToken(refresh: Boolean = true): Result<String>
expect fun firebaseAuthStateListener(onChanged: (AuthStatus) -> Unit)
expect suspend fun firebaseAuthSignOut()
package com.firebase.auth

import com.firebase.initializer.activityHolder
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.Exception

actual suspend fun sendOtpCode(
    phoneNumber: String,
    otpListener: OtpListener,
    resendToken: ResendTokenHandle?
) {
    val activity = activityHolder.get()
    val message = "Activity is not initialized when request otp."

    if (activity == null) {
        otpListener.onFailed(exception = Exception(message))
        return
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            otpListener.onInstantVerified(code = credential.smsCode)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            otpListener.onFailed(exception = e)
        }

        override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
            otpListener.onSent(
                verificationId = id,
                resendTokenHandle = ResendTokenHandle(token = token),
            )
        }
    }

    val builder = PhoneAuthOptions.newBuilder()
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(callbacks)

    resendToken?.let {
        builder.setForceResendingToken(it.token)
    }

    PhoneAuthProvider.verifyPhoneNumber(builder.build())
}

actual suspend fun verifyOtpCode(verificationId: String, otpCode: String): Result<String> {
    return runCatching {
        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        val user = Firebase.auth.signInWithCredential(credential).await().user
            ?: throw Exception("User is null after sign-in")

        user.getIdToken(true).await().token
            ?: throw Exception("Firebase ID Token is null or blank")
    }
}

actual suspend fun getFirebaseIdToken(refresh: Boolean): Result<String> {
    return runCatching {
        Firebase.auth.currentUser?.getIdToken(refresh)?.await()?.token
            ?: throw Exception("Firebase ID Token is null")
    }
}

actual fun firebaseAuthStateListener(onChanged: (AuthStatus) -> Unit) {
    Firebase.auth.addAuthStateListener { auth ->
        if (auth.currentUser == null) {
            onChanged(AuthStatus.SIGNED_OUT)
        } else {
            onChanged(AuthStatus.SIGNED_IN)
        }
    }
}

actual suspend fun firebaseAuthSignOut() {
    Firebase.auth.signOut()
}
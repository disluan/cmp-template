package com.firebase.auth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRPhoneAuthProvider
import cocoapods.FirebaseAuth.FIRUser
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual suspend fun sendOtpCode(
    phoneNumber: String,
    otpListener: OtpListener,
    resendToken: ResendTokenHandle?
) {
    FIRPhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber, null) { id, error ->
        id?.let {
            otpListener.onSent(verificationId = it, resendTokenHandle = ResendTokenHandle())
        }
        error?.let {
            otpListener.onFailed(exception = Exception(it.localizedDescription))
        }
    }
}

actual suspend fun verifyOtpCode(verificationId: String, otpCode: String): Result<String> {
    return suspendCancellableCoroutine { continuation ->
        val credential = FIRPhoneAuthProvider.provider().credentialWithVerificationID(
            verificationID = verificationId,
            verificationCode = otpCode
        )

        FIRAuth.auth().signInWithCredential(credential) { result, error ->
            val user = result?.user()
            val exception = Exception(error?.localizedDescription ?: "Failed to sign in")

            if (user == null) {
                continuation.resume(Result.failure(exception))
                return@signInWithCredential
            }

            CoroutineScope(Dispatchers.Default).launch {
                continuation.resume(value = runCatching { user.getIdToken(true) })
            }
        }
    }
}

actual suspend fun getFirebaseIdToken(refresh: Boolean): Result<String> {
    return runCatching {
        FIRAuth.auth().currentUser()?.getIdToken(refresh)
            ?: throw Exception("Firebase ID Token is null or blank")
    }
}

actual fun firebaseAuthStateListener(onChanged: (AuthStatus) -> Unit) {
    FIRAuth.auth().addAuthStateDidChangeListener { _, user ->
        if (user == null) {
            onChanged(AuthStatus.SIGNED_OUT)
        } else {
            onChanged(AuthStatus.SIGNED_IN)
        }
    }
}

@OptIn(BetaInteropApi::class)
actual suspend fun firebaseAuthSignOut() {
    return suspendCancellableCoroutine { continuation ->
        memScoped {
            val nsError = alloc<ObjCObjectVar<NSError?>>().ptr
            val success = FIRAuth.auth().signOut(nsError)

            if (success) {
                continuation.resume(Unit)
            } else {
                val message = nsError.pointed.value?.localizedDescription ?: "Sign out unknown error"
                continuation.resumeWithException(Exception(message))
            }
        }
    }
}

private suspend fun FIRUser.getIdToken(refresh: Boolean): String {
    return suspendCancellableCoroutine { cont ->
        this.getIDTokenForcingRefresh(refresh) { token, error ->
            if (!token.isNullOrBlank()) {
                cont.resume(token)
            } else {
                cont.resumeWithException(
                    Exception(error?.localizedDescription ?: "Failed to get ID token")
                )
            }
        }
    }
}
package com.firebase.auth

import com.google.firebase.auth.PhoneAuthProvider

actual class ResendTokenHandle internal constructor(
    internal val token: PhoneAuthProvider.ForceResendingToken
)
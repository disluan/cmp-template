package com.sources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.firebase.auth.Auth
import com.firebase.auth.AuthStatus
import com.firebase.auth.OtpListener
import com.firebase.auth.ResendTokenHandle
import com.firebase.notification.PushNotification
import com.sources.di.mainModule
import com.sources.di.sharedModule
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinIsolatedContext
import org.koin.compose.koinInject
import org.koin.dsl.koinApplication

@Composable
@Preview
fun App() {
    KoinIsolatedContext(context = koinApplication {
        modules(mainModule, sharedModule)
    }) {
        MaterialTheme {
            val coroutineScope = rememberCoroutineScope()
            val auth = koinInject<Auth>()

            var hasLogin by remember { mutableStateOf(false) }
            var phone by remember { mutableStateOf("") }
            var otpCode by remember { mutableStateOf("") }
            var verificationIds by remember { mutableStateOf("") }

            val notification = koinInject<PushNotification>()

            notification.fetchNewToken {
                println("Firebase Request New Token: $it")
            }

            LaunchedEffect(Unit) {
                notification.onNotificationClicked {
                    phone = it["id"].toString()
                }

                notification.onNotificationNewToken {
                    println("Firebase New Token: $it")
                }
            }

            auth.statusListener { status ->
                hasLogin = when(status) {
                    AuthStatus.SIGNED_IN -> {
                        true
                    }
                    AuthStatus.SIGNED_OUT -> {
                        false
                    }
                }
            }

            val launchVerify: () -> Unit = {
                coroutineScope.launch {
                    auth.verifyOtp(
                        verificationId = verificationIds,
                        otpCode = otpCode
                    ).onSuccess {
                        println("OTP => Firebase ID Token: $it")
                    }.onFailure {
                        println("OTP => Firebase ID Token: Failed to fetch: ${it.message}")
                    }
                }
            }

            val listener = object : OtpListener {
                override fun onSent(
                    verificationId: String,
                    resendTokenHandle: ResendTokenHandle
                ) {
                    verificationIds = verificationId
                }

                override fun onFailed(exception: Exception) {
                    println("OTP Request => Error => ${exception.message}")
                }

                override fun onInstantVerified(code: String?) {
                    otpCode = code ?: ""
                    launchVerify()
                }
            }

            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (hasLogin) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                auth.signOut()
                            }
                        }
                    ) {
                        Text("Logout")
                    }
                } else {
                    TextField(
                        value = phone,
                        onValueChange = { phone = it }
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                auth.sendOtp(
                                    phoneNumber = phone,
                                    otpListener = listener,
                                    resendToken = null
                                )
                            }
                        }
                    ) {
                        Text("Send Otp")
                    }
                    TextField(
                        value = otpCode,
                        onValueChange = { otpCode = it }
                    )
                    Button(
                        enabled = otpCode.length == 6,
                        onClick = launchVerify
                    ) {
                        Text("Verify Otp")
                    }
                }
            }
        }
    }
}
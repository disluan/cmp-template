package com.firebase.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import com.firebase.initializer.appContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class AndroidNotificationObserver : NotificationObserver {

    companion object {
        const val ACTION_NEW_FCM_TOKEN = "fcm_token_updated"
        const val ACTION_ONCLICK_NOTIFICATION = "fcm_on_click_notification"
    }

    override suspend fun observeNewToken(onNewToken: (String) -> Unit) {
        addObserverNotification(ACTION_NEW_FCM_TOKEN) { payload ->
            payload["fcmToken"]?.let { onNewToken(it) }
        }
    }

    override suspend fun observeNotification(onClick: (Map<String, String?>) -> Unit) {
        addObserverNotification(ACTION_ONCLICK_NOTIFICATION) { payload ->
            onClick(payload)
        }
    }

    private suspend fun addObserverNotification(
        name: String,
        completion: (Map<String, String?>) -> Unit
    ) {

        return callbackFlow {
            Log.d("NotificationObserver", "Broadcast received CALL")

            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.d("NotificationObserver", "✅ Received broadcast with action: ${intent?.action}")
                    Log.d("NotificationObserver", "Broadcast received for $name")

                    trySend(
                        element = intent?.extras?.keySet()?.associateWith {
                            intent.extras?.getString(it)
                        } ?: emptyMap()
                    )
                }
            }

            ContextCompat.registerReceiver(
                appContext,
                receiver,
                IntentFilter(name),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            awaitClose {
                runCatching { appContext.unregisterReceiver(receiver) }
            }
        }.collect(completion)
    }
}
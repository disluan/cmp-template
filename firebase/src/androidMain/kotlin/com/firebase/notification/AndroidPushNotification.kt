package com.firebase.notification

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.flow.filterNotNull

class AndroidPushNotification : PushNotification {

    private val notification by lazy { AndroidNotification }

    override suspend fun onNotificationClicked(onClick: (Map<String, String?>) -> Unit) {
        notification.data.filterNotNull().collect {
            onClick(it)
        }
    }

    override suspend fun onNotificationNewToken(onChanged: (String) -> Unit) {
        notification.token.filterNotNull().collect {
            onChanged(it)
        }
    }

    override fun fetchNewToken(onCompletion: (String) -> Unit) {
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("PushNotification", "Fetching token failed", task.exception)
                return@addOnCompleteListener
            }

            onCompletion(task.result)
        }
    }

    override fun subscribeToTopic(topic: String) {
        Firebase.messaging.subscribeToTopic(topic)
    }

    override fun unsubscribeToTopic(topic: String) {
        Firebase.messaging.unsubscribeFromTopic(topic)
    }
}
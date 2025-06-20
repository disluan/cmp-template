package com.firebase.notification

val notificationDataKeys = arrayOf("id", "type")

interface PushNotification {
    suspend fun onNotificationClicked(onClick: (Map<String, String?>) -> Unit)
    suspend fun onNotificationNewToken(onChanged: (String) -> Unit)
}

expect fun getPushNotification(): PushNotification
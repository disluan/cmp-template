package com.firebase.notification

val notificationDataKeys = arrayOf("id", "type")

interface PushNotification {
    suspend fun onNotificationClicked(onClick: (Map<String, String?>) -> Unit)
    suspend fun onNotificationNewToken(onChanged: (String) -> Unit)
    fun fetchNewToken(onCompletion: (String) -> Unit)
    fun subscribeToTopic(topic: String)
    fun unsubscribeToTopic(topic: String)
}

expect fun getPushNotification(): PushNotification
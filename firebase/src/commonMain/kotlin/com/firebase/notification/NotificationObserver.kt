package com.firebase.notification

interface NotificationObserver {
    suspend fun observeNewToken(onNewToken: (String) -> Unit)
    suspend fun observeNotification(onClick: (Map<String, String?>) -> Unit)
}

expect fun getNotificationObserver(): NotificationObserver
package com.firebase.notification

class AndroidNotificationObserver : NotificationObserver {
    override suspend fun observeNewToken(onNewToken: (String) -> Unit) {

    }

    override suspend fun observeNotification(onClick: (Map<String, String?>) -> Unit) {

    }
}
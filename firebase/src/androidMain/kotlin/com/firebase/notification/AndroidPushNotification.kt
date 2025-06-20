package com.firebase.notification

import kotlinx.coroutines.flow.filterNotNull

class AndroidPushNotification : PushNotification {

    private val notification by lazy { NotificationObserver }

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
}
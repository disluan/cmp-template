package com.firebase.notification

actual fun getNotificationObserver(): NotificationObserver {
    return IosNotificationObserver()
}
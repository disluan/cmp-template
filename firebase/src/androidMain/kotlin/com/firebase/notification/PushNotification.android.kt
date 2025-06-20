package com.firebase.notification

actual fun getPushNotification(): PushNotification {
    return AndroidPushNotification()
}
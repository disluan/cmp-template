package com.firebase.notification

import platform.Foundation.NSNotificationCenter

class IosNotificationObserver: NotificationObserver {
    override suspend fun observeNewToken(onNewToken: (String) -> Unit) {
        addObserverNotification(name = "onNewToken") { payload ->
            (payload["fcmToken"] as? String)?.let { onNewToken(it) }
        }
    }

    override suspend fun observeNotification(onClick: (Map<String, String?>) -> Unit) {
        addObserverNotification("onNotificationClicked") { payload ->
            val map = payload.mapNotNull { data ->
                (data.key as? String)?.takeIf { it.isNotBlank() }?.let { key ->
                    key to data.value?.toString()
                }
            }.toMap()

            onClick(map)
        }
    }

    private fun addObserverNotification(name: String, completion: (Map<Any?, *>) -> Unit) {
        NSNotificationCenter.defaultCenter.addObserverForName(name, null, null) {
            it?.userInfo?.let { payload ->
                completion(payload)
            }
        }
    }
}
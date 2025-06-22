package com.firebase.notification

import cocoapods.FirebaseMessaging.FIRMessaging
import platform.Foundation.NSNotificationCenter
import platform.darwin.NSObjectProtocol

class IosPushNotification : PushNotification {

    override suspend fun onNotificationClicked(onClick: (Map<String, String?>) -> Unit) {
        if (notificationDataObserver != null) return

        notificationDataObserver = addObserver("onNotificationClicked") { payload ->
            onClick(notificationDataKeys.associateWith { payload[it] as? String? })
        }
    }

    override suspend fun onNotificationNewToken(onChanged: (String) -> Unit) {
        if (notificationTokenObserver != null) return

        notificationTokenObserver = addObserver("onNewToken") { payload ->
            onChanged(payload["fcmToken"] as String)
        }
    }

    override fun fetchNewToken(onCompletion: (String) -> Unit) {
        FIRMessaging.messaging().tokenWithCompletion { token, error ->
            if (error != null) {
                println("PushNotification: Fetching token failed: ${error.localizedDescription}")
                return@tokenWithCompletion
            }

            token?.let { onCompletion(it) }
        }
    }

    override fun subscribeToTopic(topic: String) {
        FIRMessaging.messaging().subscribeToTopic(topic)
    }

    override fun unsubscribeToTopic(topic: String) {
        FIRMessaging.messaging().unsubscribeFromTopic(topic)
    }

    private fun addObserver(
        name: String,
        completion: (Map<Any?, *>) -> Unit
    ): NSObjectProtocol {
        return NSNotificationCenter.Companion.defaultCenter.addObserverForName(
            name = name,
            `object` = null,
            queue = null
        ) {
            it?.userInfo?.let(completion)
        }
    }

    companion object {

        private var notificationDataObserver: NSObjectProtocol? = null
        private var notificationTokenObserver: NSObjectProtocol? = null

        fun disposeObserver() {
            notificationDataObserver?.let {
                NSNotificationCenter.defaultCenter.removeObserver(it)
                notificationDataObserver = null
            }
            notificationTokenObserver?.let {
                NSNotificationCenter.defaultCenter.removeObserver(it)
                notificationTokenObserver = null
            }
        }

    }
}
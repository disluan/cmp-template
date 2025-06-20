package com.firebase.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.firebase.initializer.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        NotificationReceiver.postToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = manager.createChannelOnNougat()

        remoteMessage.notification?.let { notification ->
            manager.showNotification(
                intent = getPendingIntent(remoteMessage.data),
                channelId = channelId,
                notification = notification
            )
        }
    }

    private fun getPendingIntent(data: Map<String, String?>): PendingIntent {
        val launchIntent = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
            ?.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                data.forEach { (key, value) ->
                    putExtra(key, value)
                }
            }

        return PendingIntent.getActivity(
            applicationContext,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun NotificationManager.showNotification(
        intent: PendingIntent,
        channelId: String,
        notification: RemoteMessage.Notification,
    ) {
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(Notification.iconId)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setContentIntent(intent)
            .build()

        notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun NotificationManager.createChannelOnNougat(): String {
        val channelId = "general"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            createNotificationChannel(channel)
        }

        return channelId
    }
}
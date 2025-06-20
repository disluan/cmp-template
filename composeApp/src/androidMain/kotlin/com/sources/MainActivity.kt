package com.sources

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import com.firebase.initializer.Notification
import com.firebase.notification.AndroidNotification
import com.firebase.notification.notificationDataKeys

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showRejectedNotificationPermission()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        onNewIntent(intent)
        Notification.initialize(iconId = R.drawable.ic_notifications)

        setContent {
            App()
        }

        askNotificationPermission()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val bundle = intent.extras ?: return
        val notificationData = mutableMapOf<String, String?>()

        notificationDataKeys.forEach { key ->
            notificationData.put(key, bundle.getString(key))
        }

        AndroidNotification.setData(notificationData)
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showRejectedNotificationPermission() {
        Toast.makeText(
            this,
            R.string.notification_permission_rejected,
            Toast.LENGTH_LONG
        ).show()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
package com.sources.utilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.firebase.notification.NotificationData
import com.firebase.notification.NotificationReceiver
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import org.koin.compose.koinInject

@Composable
fun LaunchDetailsNotification(onChanged: (NotificationData) -> Unit) {
    val notification = koinInject<NotificationReceiver>()

    LaunchedEffect(Unit) {
        notification.data
            .filterNotNull()
            .distinctUntilChanged()
            .collect {
                onChanged(it)
                notification.postNotification(null)
            }
    }
}
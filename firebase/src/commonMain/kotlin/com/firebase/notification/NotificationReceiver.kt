package com.firebase.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotificationData(val id: Int, val type: String)

object NotificationReceiver {

    val dataKeys = arrayOf("id", "type")

    private val _data = MutableStateFlow<NotificationData?>(null)
    val data = _data.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    fun postNotification(data: Map<String, String?>?) {
        if (_data.value != data) {
            _data.update {
                if (data != null) {
                    NotificationData(id = data["id"]?.toInt() ?: -1, type = data["type"].orEmpty())
                } else {
                    null
                }
            }
        }
    }

    fun postToken(token: String?) {
        _token.update { token }
    }
}
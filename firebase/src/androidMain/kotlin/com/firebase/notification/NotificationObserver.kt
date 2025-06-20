package com.firebase.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object NotificationObserver {
    private val _data = MutableStateFlow<Map<String, String?>?>(null)
    val data = _data.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    fun setData(data: Map<String, String?>?) {
        _data.update { data }
    }

    fun setToken(token: String?) {
        _token.update { token }
    }
}
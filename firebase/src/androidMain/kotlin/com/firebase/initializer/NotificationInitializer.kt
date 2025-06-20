package com.firebase.initializer

object Notification {
    private var _notificationIconId: Int? = null

    val iconId: Int
        get() = _notificationIconId
            ?: error("Notification icon has not been initialized." +
                    " Call Notification.initialize(R.drawable.ic_notification) from the app module.")

    fun initialize(iconId: Int) {
        _notificationIconId = iconId
    }
}
package com.firebase.di

import com.firebase.auth.Auth
import com.firebase.auth.AuthImpl
import com.firebase.notification.NotificationObserver
import com.firebase.notification.getNotificationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseModule = module {
    singleOf(::AuthImpl).bind<Auth>()
    singleOf(::getNotificationObserver).bind<NotificationObserver>()
}
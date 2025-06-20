package com.firebase.di

import com.firebase.auth.Auth
import com.firebase.auth.AuthImpl
import com.firebase.notification.NotificationReceiver
import org.koin.dsl.module

val firebaseModule = module {
    single<Auth> { AuthImpl() }
    single<NotificationReceiver> { NotificationReceiver }
}
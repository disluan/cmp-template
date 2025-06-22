package com.core.di

import com.core.network.HttpClient
import com.core.network.Repository
import com.firebase.di.firebaseModule
import org.koin.dsl.module

val coreModule = module {
    includes(firebaseModule)
    single { HttpClient.get(get()) }
    single { Repository(get()) }
}
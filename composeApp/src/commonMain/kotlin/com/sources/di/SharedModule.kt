package com.sources.di

import com.firebase.di.firebaseModule
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule: Module = module {
    includes(firebaseModule)
}
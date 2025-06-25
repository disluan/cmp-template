package com.sources.di

import com.sources.screens.login.LoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule: Module = module {
    viewModel { LoginViewModel(get()) }
}
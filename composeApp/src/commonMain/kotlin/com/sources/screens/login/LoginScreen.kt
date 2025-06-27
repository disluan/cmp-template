package com.sources.screens.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen() {
    val viewModel = koinViewModel<LoginViewModel>()

    Text("Hola")
}

@Composable
private fun LoginContentUI() {
    Text("Testing")
}

@Preview
@Composable
fun LoginPreviewScreen() {
    LoginScreen()
}
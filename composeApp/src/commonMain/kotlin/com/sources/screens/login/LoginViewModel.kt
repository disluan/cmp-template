package com.sources.screens.login

import com.core.network.NoContent
import com.core.network.Repository
import com.core.viewmodel.ViewModelOf
import com.core.viewmodel.onLoad
import com.sources.constants.ApiUrl

class LoginViewModel(
    private val repository: Repository
): ViewModelOf<LoginContract.State, LoginContract.Action, LoginContract.Effect>(
    initial = LoginContract.State.Initial
) {

    override fun onAction(action: LoginContract.Action) {
        when(action) {
            is LoginContract.Action.CheckNumber -> checkNumber(action.phone)
            is LoginContract.Action.OpenOtpPage -> openOtpPage(action.phone)
        }
    }

    private fun checkNumber(phone: String) {
        launchWithRetry(onRetry = { checkNumber(phone) }) {
            repository.get<NoContent>(url = ApiUrl.Auth.CHECK_NUMBER)
                .onLoad { updateState { copy(loading = it) } }
                .collect { updateState { copy(exist = true) } }
        }
    }

    private fun openOtpPage(phone: String) {
        sendEffect(LoginContract.Effect.NavigateToOtp(phone))
    }
}
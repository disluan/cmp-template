package com.sources.screens.login

import androidx.compose.runtime.Immutable
import com.core.viewmodel.ViewAction
import com.core.viewmodel.ViewEffect
import com.core.viewmodel.ViewState

object LoginContract {
    @Immutable
    data class State(
        override val loading: Boolean,
        val phone: String,
        val exist: Boolean,
        val message: String
    ): ViewState {
        companion object {
            val Initial = State(false, phone = "", exist = false, message = "")
        }
    }

    sealed class Action: ViewAction {
        data class CheckNumber(val phone: String): Action()
        data class OpenOtpPage(val phone: String): Action()
    }

    sealed class Effect: ViewEffect {
        data class NavigateToOtp(val phone: String): Effect()
    }
}
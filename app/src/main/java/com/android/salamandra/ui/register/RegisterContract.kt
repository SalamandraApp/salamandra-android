package com.android.salamandra.ui.register

import com.android.salamandra.core.boilerplate.Event
import com.android.salamandra.ui.UiText
import com.android.salamandra.core.boilerplate.Intent
import com.android.salamandra.core.boilerplate.State
import com.android.salamandra.ui.login.LoginEvent
import com.android.salamandra.ui.login.LoginIntent


data class RegisterState(
    val loading: Boolean,
    val error: UiText?,
    val username: String,
    val email: String,
    val password: String,
    val isEmailValid: Boolean,
    val passwordFormatError: UiText?,
) : State {
    companion object {
        val initial: RegisterState = RegisterState(
            loading = false,
            error = null,
            username = "",
            email = "",
            password = "",
            isEmailValid = true,
            passwordFormatError = null
        )
    }
}


sealed class RegisterIntent: Intent {
    data class Loading(val isLoading: Boolean): RegisterIntent()
    data class Error(val error: UiText): RegisterIntent()
    data object CloseError: RegisterIntent()
    data class ChangeEmail(val email: String): RegisterIntent()
    data class ChangePassword(val password: String): RegisterIntent()
    data class ChangeUsername(val username: String): RegisterIntent()
    data class OnRegister(val username: String, val email: String, val password: String): RegisterIntent()
    data object GoToSignIn: RegisterIntent()
}
sealed class RegisterEvent: Event {
    data object NavigateToLogin: RegisterEvent()
    data object NavigateToVerifyCode: RegisterEvent()
}
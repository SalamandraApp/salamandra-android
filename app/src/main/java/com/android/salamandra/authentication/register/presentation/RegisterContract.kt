package com.android.salamandra.authentication.register.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError


data class RegisterState(
    val loading: Boolean,
    val error: RootError?,
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
    data class Error(val error: RootError): RegisterIntent()
    data object CloseError: RegisterIntent()
    data class ChangeEmail(val email: String): RegisterIntent()
    data class ChangePassword(val password: String): RegisterIntent()
    data class ChangeUsername(val username: String): RegisterIntent()
    data object OnRegister: RegisterIntent()
    data object GoToSignIn: RegisterIntent()
}
sealed class RegisterEvent: Event {
    data object NavigateToLogin: RegisterEvent()
    data object NavigateToVerifyCode: RegisterEvent()
}
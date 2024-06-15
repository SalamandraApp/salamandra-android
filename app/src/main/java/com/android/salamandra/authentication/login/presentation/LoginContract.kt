package com.android.salamandra.authentication.login.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State


data class LoginState(
    val email: String,
    val password: String,
    val loading: Boolean,
    val error: RootError?,
) : State {
    companion object {
        val initial: LoginState = LoginState(
            email = "",
            password = "",
            loading = false,
            error = null,
        )
    }
}


sealed class LoginIntent: Intent {
    data class Loading(val isLoading: Boolean): LoginIntent()
    data class Error(val error: RootError): LoginIntent()
    data object Login: LoginIntent()
    data object CloseError: LoginIntent()
    data class ChangeEmail(val email: String): LoginIntent()
    data class ChangePassword(val password: String): LoginIntent()
    data object GoToSignup: LoginIntent()
}

sealed class LoginEvent: Event {
    data object NavigateToSignUp: LoginEvent()
    data object NavigateToHome: LoginEvent()
}
package com.android.salamandra.authentication.login.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State


data class LoginState(
    val email: String,
    val password: String,
    val error: RootError?,
) : State {
    companion object {
        val initial: LoginState = LoginState(
            email = "",
            password = "",
            error = null,
        )
    }
}


sealed class LoginIntent: Intent {
    data class Error(val error: RootError): LoginIntent()
    data object Login: LoginIntent()
    data object CloseError: LoginIntent()
    data class ChangeEmail(val email: String): LoginIntent()
    data class ChangePassword(val password: String): LoginIntent()
    data object GoToSignup: LoginIntent()
    data object GoToHomeNoSignIn: LoginIntent()
}

sealed class LoginEvent: Event {
    data object NavigateToSignUp: LoginEvent()
    data object NavigateToProfile: LoginEvent()
}
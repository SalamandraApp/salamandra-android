package com.android.salamandra.ui.login

import com.android.salamandra.domain.error.RootError
import com.android.salamandra.ui.UiText
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class LoginState(
    val email: String,
    val password: String,
    val loading: Boolean,
    val error: RootError?,
    val success: Boolean,
) : State {
    companion object {
        val initial: LoginState = LoginState(
            email = "",
            password = "",
            loading = false,
            error = null,
            success = false
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
}
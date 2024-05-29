package com.android.salamandra.ui.login

import com.android.salamandra.ui.UiText
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class LoginState(
    val loading: Boolean,
    val error: UiText?,
    val success: Boolean,
) : State {
    companion object {
        val initial: LoginState = LoginState(
            loading = false,
            error = null,
            success = false
        )
    }
}


sealed class LoginIntent: Intent {
    data class Loading(val isLoading: Boolean): LoginIntent()
    data class Error(val errorMsg: UiText): LoginIntent()
    data object Login: LoginIntent()
    data object CloseError: LoginIntent()
}
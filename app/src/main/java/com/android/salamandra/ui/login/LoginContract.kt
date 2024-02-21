package com.android.salamandra.ui.login

import com.android.salamandra.domain.model.UiError
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class LoginState(
    val loading: Boolean,
    val error: UiError,
    val success: Boolean,
) : State {
    companion object {
        val initial: LoginState = LoginState(
            loading = false,
            error = UiError(false, null),
            success = false
        )
    }
}


sealed class LoginIntent: Intent {
    data object Loading: LoginIntent()
    data class Error(val errorMsg: String): LoginIntent()
    data object Login: LoginIntent()
    data object CloseError: LoginIntent()
}
package com.android.salamandra.ui.login

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class LoginState(
    val loading: Boolean,
    val error: Error,
    val success: Boolean
) : State {
    companion object {
        val initial: LoginState = LoginState(
            loading = false,
            error = Error(false, null),
            success = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class LoginIntent: Intent {
    data object Loading: LoginIntent()
    data class Error(val errorMsg: String): LoginIntent()
    data object Success: LoginIntent()
}
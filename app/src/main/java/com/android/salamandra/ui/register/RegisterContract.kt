package com.android.salamandra.ui.register

import com.android.salamandra.ui.login.Error
import com.android.salamandra.ui.login.LoginIntent
import com.android.salamandra.ui.login.LoginState
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class RegisterState(
    val loading: Boolean,
    val error: Error,
    val success: Boolean
) : State {
    companion object {
        val initial: RegisterState = RegisterState(
            loading = false,
            error = Error(false, null),
            success = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class RegisterIntent: Intent {
    data object Loading: RegisterIntent()
    data class Error(val errorMsg: String): RegisterIntent()
    data object Success: RegisterIntent()
}
package com.android.salamandra.ui.register

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class RegisterState(
    val loading: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: RegisterState = RegisterState(
            loading = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class RegisterIntent: Intent {
    data object Loading: RegisterIntent()
    data class Error(val errorMsg: String): RegisterIntent()
}
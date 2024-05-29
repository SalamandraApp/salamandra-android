package com.android.salamandra.ui.register

import com.android.salamandra.ui.UiText
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class RegisterState(
    val loading: Boolean,
    val error: UiText?,
    val newErrorType: UiText?,
    val success: Boolean,
    val confirmScreen: Boolean
) : State {
    companion object {
        val initial: RegisterState = RegisterState(
            loading = false,
            error = null,
            success = false,
            confirmScreen = false,
            newErrorType = null
        )
    }
}


sealed class RegisterIntent: Intent {
    data class Loading(val isLoading: Boolean): RegisterIntent()
    data class Error(val error: UiText): RegisterIntent()
    data object Success: RegisterIntent()
    data object ConfirmCode: RegisterIntent()
    data object CloseError: RegisterIntent()
}
package com.android.salamandra.ui.register

import com.android.salamandra.ui.UiText
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class RegisterState(
    val loading: Boolean,
    val error: UiText?,
    val success: Boolean,
    val confirmScreen: Boolean,
    val username: String,
    val email: String,
    val password: String,
    val code: String,
    val isEmailValid: Boolean,
    val passwordFormatError: UiText?,
) : State {
    companion object {
        val initial: RegisterState = RegisterState(
            loading = false,
            error = null,
            success = false,
            confirmScreen = false,
            username = "",
            email = "",
            password = "",
            code = "",
            isEmailValid = true,
            passwordFormatError = null
        )
    }
}


sealed class RegisterIntent: Intent {
    data class Loading(val isLoading: Boolean): RegisterIntent()
    data class Error(val error: UiText): RegisterIntent()
    data object Success: RegisterIntent()
    data object ConfirmCode: RegisterIntent()
    data object CloseError: RegisterIntent()
    data class ChangeEmail(val email: String): RegisterIntent()
    data class ChangePassword(val password: String): RegisterIntent()
    data class ChangeUsername(val username: String): RegisterIntent()
    data class ChangeCode(val code: String): RegisterIntent()
    data class OnRegister(val username: String, val email: String, val password: String): RegisterIntent()
}
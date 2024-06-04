package com.android.salamandra.ui.register

import androidx.lifecycle.viewModelScope
import com.android.salamandra.core.boilerplate.BaseViewModel
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.UserDataValidator
import com.android.salamandra.domain.error.Result
import com.android.salamandra.ui.UiText
import com.android.salamandra.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: Repository,
    private val userDataValidator: UserDataValidator
) : BaseViewModel<RegisterState, RegisterIntent>(RegisterState.initial) {


    override fun reduce(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Error -> onError(intent.error)

            is RegisterIntent.CloseError -> onCloseError()

            is RegisterIntent.Loading -> onLoading(intent.isLoading)

            RegisterIntent.ConfirmCode -> onVerifyCode()

            RegisterIntent.Success -> _state.update { it.copy(success = true) }

            is RegisterIntent.ChangeEmail -> validateEmail(intent.email)

            is RegisterIntent.ChangePassword -> validatePassword(intent.password)

            is RegisterIntent.ChangeUsername -> _state.update { it.copy(username = intent.username) }

            is RegisterIntent.ChangeCode -> _state.update { it.copy(code = intent.code) }

            is RegisterIntent.OnRegister -> onRegister()
        }
    }

    private fun onError(error: UiText) =
        _state.update { it.copy(error = error) }

    private fun onCloseError() =
        _state.update { it.copy(error = null) }

    private fun onLoading(isLoading: Boolean) =
        _state.update { it.copy(loading = isLoading) }

    private fun onRegister() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val register =
                repository.register(
                    email = state.value.email,
                    password = state.value.password,
                    username = state.value.username
                )) {
                is Result.Success -> _state.update { it.copy(confirmScreen = true) }
                is Result.Error -> TODO()
            }
        }
    }

    private fun onVerifyCode() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val confirmation =
                repository.confirmRegister(
                    username = state.value.username,
                    code = state.value.code
                )) {
                is Result.Error -> TODO()
                is Result.Success -> dispatch(RegisterIntent.Success)
            }
        }
    }

    private fun validatePassword(password: String) {
        when (val result = userDataValidator.validatePassword(password)) {
            is Result.Success -> _state.update { it.copy(passwordFormatError = null) }
            is Result.Error -> _state.update { it.copy(passwordFormatError = result.error.asUiText()) }
        }
        _state.update { it.copy(password = password) }
    }

    private fun validateEmail(email: String) {
        if (!userDataValidator.validateEmail(email)) _state.update { it.copy(isEmailValid = false) }
        _state.update { it.copy(email = email) }
    }
}

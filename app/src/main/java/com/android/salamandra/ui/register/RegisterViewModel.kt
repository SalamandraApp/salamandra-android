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

            RegisterIntent.Success -> state = state.copy(success = true)

            is RegisterIntent.ChangeEmail -> validateEmail(intent.email)

            is RegisterIntent.ChangePassword -> validatePassword(intent.password)

            is RegisterIntent.ChangeUsername -> state = state.copy(username = intent.username)

            is RegisterIntent.ChangeCode -> state = state.copy(code = intent.code)

            is RegisterIntent.OnRegister -> onRegister()
        }
    }

    private fun onError(error: UiText) {
        state = state.copy(error = error)
    }

    private fun onCloseError() {
        state = state.copy(error = null)
    }

    private fun onLoading(isLoading: Boolean) {
        state = state.copy(loading = isLoading)
    }

    private fun onRegister() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val register =
                repository.register(
                    email = state.email,
                    password = state.password,
                    username = state.username
                )) {
                is Result.Success -> state = state.copy(confirmScreen = true)
                is Result.Error -> TODO()
            }
        }
    }

    private fun onVerifyCode() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val confirmation =
                repository.confirmRegister(username = state.username, code = state.code)) {
                is Result.Error -> TODO()
                is Result.Success -> dispatch(RegisterIntent.Success)
            }
        }
    }

    private fun validatePassword(password: String) {
        state = when (val result = userDataValidator.validatePassword(password)) {
            is Result.Success -> state.copy(passwordFormatError = null)
            is Result.Error -> state.copy(passwordFormatError = result.error.asUiText())
        }
        state = state.copy(password = password)
    }

    private fun validateEmail(email:String){
        if(!userDataValidator.validateEmail(email)) state = state.copy(isEmailValid = false)
        state = state.copy(email = email)
    }
}

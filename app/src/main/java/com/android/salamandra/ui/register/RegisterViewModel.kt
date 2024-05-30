package com.android.salamandra.ui.register

import androidx.lifecycle.viewModelScope
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.UserDataValidator
import com.android.salamandra.domain.error.Result
import com.android.salamandra.ui.asUiText
import com.android.salamandra.core.boilerplate.BaseViewModel
import com.android.salamandra.core.boilerplate.template.tIntent
import com.android.salamandra.ui.UiText
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
            RegisterIntent.ConfirmCode -> TODO()
            RegisterIntent.Success -> TODO()
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

    private fun onRegisterClick(password: String) {
        when (val result = userDataValidator.validatePassword(password)) {
            is Result.Error -> state = state.copy(error = result.error.asUiText())

            is Result.Success -> {

            }
        }

        //add errors for network call... etc
    }
}

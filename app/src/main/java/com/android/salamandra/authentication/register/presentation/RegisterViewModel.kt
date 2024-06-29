package com.android.salamandra.authentication.register.presentation

import com.android.salamandra.authentication.register.domain.Repository
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.UserDataValidator
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.presentation.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: Repository,
    ioDispatcher: CoroutineDispatcher,
    private val userDataValidator: UserDataValidator
) : BaseViewModel<RegisterState, RegisterIntent, RegisterEvent>(RegisterState.initial, ioDispatcher) {


    override fun reduce(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Error -> onError(intent.error)

            is RegisterIntent.CloseError -> onCloseError()

            is RegisterIntent.Loading -> onLoading(intent.isLoading)

            is RegisterIntent.ChangeEmail -> validateEmail(intent.email)

            is RegisterIntent.ChangePassword -> validatePassword(intent.password)

            is RegisterIntent.ChangeUsername -> _state.update { it.copy(username = intent.username) }

            is RegisterIntent.OnRegister -> onRegister()

            RegisterIntent.GoToSignIn -> sendEvent(RegisterEvent.NavigateToLogin)
            RegisterIntent.GoToHomeNoRegister -> sendEvent(RegisterEvent.NavigateToProfile)
        }
    }

    private fun onError(error: RootError) =
        _state.update { it.copy(error = error) }

    private fun onCloseError() =
        _state.update { it.copy(error = null) }

    private fun onLoading(isLoading: Boolean) =
        _state.update { it.copy(loading = isLoading) }

    private fun onRegister() {
        ioLaunch {
            when (val register =
                repository.register(
                    email = state.value.email,
                    password = state.value.password,
                    username = state.value.username
                )) {
                is Result.Success -> sendEvent(RegisterEvent.NavigateToVerifyCode)
                is Result.Error ->  _state.update { it.copy(error = register.error) }
            }
        }
    }

    private fun validatePassword(password: String) {
        when (val result = userDataValidator.validatePassword(password)) {
            is Result.Success -> _state.update { it.copy(passwordFormatError = null) }
            is Result.Error -> _state.update { it.copy(passwordFormatError = result.error) }
        }
        _state.update { it.copy(password = password) }
    }

    private fun validateEmail(email: String) {
        if (!userDataValidator.validateEmail(email)) _state.update { it.copy(isEmailValid = false) }
        else _state.update { it.copy(isEmailValid = true) }
        _state.update { it.copy(email = email) }
    }
}

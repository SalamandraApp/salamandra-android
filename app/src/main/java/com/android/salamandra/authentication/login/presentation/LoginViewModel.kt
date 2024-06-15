package com.android.salamandra.authentication.login.presentation

import com.android.salamandra.authentication.login.domain.Repository
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.error.RootError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    ioDispatcher: CoroutineDispatcher
) :
    BaseViewModel<LoginState, LoginIntent, LoginEvent>(LoginState.initial, ioDispatcher) {

    override fun reduce(
        intent: LoginIntent
    ) { //This function reduces each intent with a when
        when (intent) {
            is LoginIntent.Error -> onError(intent.error)

            is LoginIntent.CloseError -> onCloseError()

            is LoginIntent.Loading -> onLoading(intent.isLoading)

            is LoginIntent.Login -> onLogin()

            is LoginIntent.ChangeEmail -> onChangeEmail(intent.email)

            is LoginIntent.ChangePassword -> onChangePassword(intent.password)

            is LoginIntent.GoToSignup -> sendEvent(LoginEvent.NavigateToSignUp)

        }
    }

    private fun onError(error: RootError) = _state.update { it.copy(error = error) }


    private fun onCloseError() = _state.update { it.copy(error = null) }


    private fun onLoading(isLoading: Boolean) = _state.update { it.copy(loading = isLoading) }


    private fun onLogin() {
        ioLaunch {
            when (val result =
                repository.login(email = state.value.email, password = state.value.password)) {
                is Result.Success -> {
                    sendEvent(LoginEvent.NavigateToHome)
                }

                is Result.Error -> _state.update { it.copy(error = result.error) }
            }
        }
    }

    private fun onChangePassword(password: String) = _state.update { it.copy(password = password) }


    private fun onChangeEmail(email: String) = _state.update { it.copy(email = email) }

}
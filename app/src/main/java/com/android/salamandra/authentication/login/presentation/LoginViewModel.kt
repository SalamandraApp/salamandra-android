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
            is LoginIntent.Error -> _state.update { it.copy(error = intent.error) }

            is LoginIntent.CloseError -> _state.update { it.copy(error = null) }

            is LoginIntent.Login -> onLogin()

            is LoginIntent.ChangeEmail -> _state.update { it.copy(email = intent.email) }

            is LoginIntent.ChangePassword -> _state.update { it.copy(password = intent.password) }

            is LoginIntent.GoToSignup -> sendEvent(LoginEvent.NavigateToSignUp)

            LoginIntent.GoToHomeNoSignIn -> sendEvent(LoginEvent.NavigateToProfile)
        }
    }

    private fun onLogin() {
        ioLaunch {
            when (val result =
                repository.login(email = state.value.email, password = state.value.password)) {
                is Result.Success -> {
                    sendEvent(LoginEvent.NavigateToProfile)
                }

                is Result.Error -> _state.update { it.copy(error = result.error) }
            }
        }
    }


}
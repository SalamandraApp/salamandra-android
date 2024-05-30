package com.android.salamandra.ui.login

import androidx.lifecycle.viewModelScope
import com.android.salamandra.core.boilerplate.BaseViewModel
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.error.Result
import com.android.salamandra.ui.UiText
import com.android.salamandra.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<LoginState, LoginIntent>(LoginState.initial) {

    override fun reduce(
        intent: LoginIntent
    ) { //This function reduces each intent with a when
        when (intent) {
            is LoginIntent.Error -> onError(intent.error)
            is LoginIntent.CloseError -> onCloseError()
            is LoginIntent.Loading -> onLoading(intent.isLoading)
            LoginIntent.Login -> onLogin()
            is LoginIntent.SetEmail -> onChangeEmail(intent.email)
            is LoginIntent.SetPassword -> onChangePassword(intent.password)
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

    private fun onLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            state = when (val result = repository.login(email = state.email, password = state.password)) {
                is Result.Success -> state.copy(success = true)
                is Result.Error -> state.copy(error = result.error.asUiText())
            }
        }
    }

    private fun onChangePassword(password: String) {
        state = state.copy(password = password)
    }

    private fun onChangeEmail(email: String) {
        state = state.copy(email = email)
    }
}
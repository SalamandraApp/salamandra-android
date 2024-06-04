package com.android.salamandra.ui.login

import androidx.lifecycle.viewModelScope
import com.android.salamandra.core.boilerplate.BaseViewModel
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.error.Result
import com.android.salamandra.ui.UiText
import com.android.salamandra.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
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
            is LoginIntent.ChangeEmail -> onChangeEmail(intent.email)
            is LoginIntent.ChangePassword -> onChangePassword(intent.password)
        }
    }

    private fun onError(error: UiText) = _state.update { it.copy(error = error) }


    private fun onCloseError() = _state.update { it.copy(error = null) }


    private fun onLoading(isLoading: Boolean) = _state.update { it.copy(loading = isLoading) }


    private fun onLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result =
                repository.login(email = state.value.email, password = state.value.password)) {
                is Result.Success -> _state.update { it.copy(success = true) }
                is Result.Error -> _state.update { it.copy(error = result.error.asUiText()) }
            }
        }
    }

    private fun onChangePassword(password: String) = _state.update { it.copy(password = password) }


    private fun onChangeEmail(email: String) = _state.update { it.copy(email = email) }

}
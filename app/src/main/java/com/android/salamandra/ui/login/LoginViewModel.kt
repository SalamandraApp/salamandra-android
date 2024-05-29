package com.android.salamandra.ui.login

import androidx.lifecycle.viewModelScope
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.error.Result
import com.android.salamandra.ui.asUiText
import com.android.salamandra.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<LoginState, LoginIntent>(LoginState.initial) {

    override fun reduce(
        state: LoginState,
        intent: LoginIntent
    ): LoginState { //This function reduces each intent with a when
        return when (intent) {
            is LoginIntent.Error -> state.copy(
                error = intent.errorMsg,
                loading = false
            )

            is LoginIntent.Loading -> state.copy(
                loading = true
            )

            is LoginIntent.Login -> state.copy(
                loading = false,
                success = true
            )

            is LoginIntent.CloseError -> state.copy(
                error = null,
                loading = false,
                success = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onLogin(email: String, password: String) {
        dispatch(LoginIntent.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.login(email = email, password = password)){
                is Result.Success -> dispatch(LoginIntent.Login)
                is Result.Error -> dispatch(LoginIntent.Error(result.error.asUiText()))
            }
        }
    }

    fun onCloseDialog() = dispatch(LoginIntent.CloseError)
}
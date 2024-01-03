package com.android.salamandra.ui.login

import androidx.lifecycle.viewModelScope
import com.android.salamandra.domain.usecases.LoginUseCase
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase): BaseViewModel<LoginState, LoginIntent>(LoginState.initial) {

    override fun reduce(state: LoginState, intent: LoginIntent): LoginState { //This function reduces each intent with a when
        return when(intent){
            is LoginIntent.Error -> state.copy(
                error = Error(isError = true, errorMsg = intent.errorMsg),
                loading = false
            )

            is LoginIntent.Loading -> state.copy(
                loading = true
            )

            is LoginIntent.Success ->state.copy(
                loading = false,
                success = true
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onLogin(username: String, password: String){
        dispatch(LoginIntent.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(username, password) {
                dispatch(LoginIntent.Success)
            }
        }
    }
}
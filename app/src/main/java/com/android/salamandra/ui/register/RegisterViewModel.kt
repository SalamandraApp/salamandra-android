package com.android.salamandra.ui.register

import androidx.lifecycle.viewModelScope
import com.android.salamandra.domain.model.UiError
import com.android.salamandra.domain.usecases.RegisterUseCase
import com.android.salamandra.ui.login.LoginIntent
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel<RegisterState, RegisterIntent>(RegisterState.initial) {

    override fun reduce(
        state: RegisterState,
        intent: RegisterIntent
    ): RegisterState { //This function reduces each intent with a when
        return when(intent){
            is RegisterIntent.Error -> state.copy(
                error = UiError(isError = true, errorMsg = intent.errorMsg),
                loading = false
            )

            is RegisterIntent.Loading -> state.copy(
                loading = true
            )

            is RegisterIntent.Success ->state.copy(
                loading = false,
                success = true
            )

            is RegisterIntent.ConfirmCode -> state.copy(loading = false, confirmScreen = true)

            is RegisterIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false,
                success = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onRegister(username: String, email: String, password: String){
        dispatch(RegisterIntent.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                registerUseCase.register(username = username, email = email, password = password)
                dispatch(RegisterIntent.ConfirmCode)
            } catch(e: Exception){
                dispatch(RegisterIntent.Error(e.message.orEmpty()))
            }
        }
    }

    fun onVerifyCode(username: String, code: String){
        dispatch(RegisterIntent.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                registerUseCase.confirmRegister(username = username, code = code)
                dispatch(RegisterIntent.Success)
            } catch(e: Exception){
                dispatch(RegisterIntent.Error(e.message.orEmpty()))
            }
        }
    }

    fun onCloseDialog() = dispatch(RegisterIntent.CloseError)


}
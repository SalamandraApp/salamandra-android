package com.android.salamandra.ui.register

import androidx.lifecycle.viewModelScope
import com.android.salamandra.domain.UserDataValidator
import com.android.salamandra.domain.error.Result
import com.android.salamandra.domain.model.UiError
import com.android.salamandra.domain.usecases.auth.RegisterUseCase
import com.android.salamandra.ui.asUiText
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val userDataValidator: UserDataValidator
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

            is RegisterIntent.NewError -> state.copy(newErrorType = intent.error)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onRegister(username: String, email: String, password: String){
        dispatch(RegisterIntent.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                registerUseCase.register(username = username, email = email, password = password){
                    dispatch(RegisterIntent.ConfirmCode)
                }
            } catch(e: Exception){
                dispatch(RegisterIntent.Error(e.message.orEmpty()))
            }
        }
    }

    fun onVerifyCode(username: String, code: String){
        dispatch(RegisterIntent.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                registerUseCase.confirmRegister(username = username, code = code){
                    dispatch(RegisterIntent.Success)
                }
            } catch(e: Exception){
                dispatch(RegisterIntent.Error(e.message.orEmpty()))
            }
        }
    }

    fun onRegisterClick(password: String){
        when(val result = userDataValidator.validatePassword(password)){
            is Result.Error -> {
                dispatch(RegisterIntent.NewError(result.error.asUiText()))
            }
            is Result.Success -> {

            }
        }

        //add errors for network call... etc
    }

    fun onCloseDialog() = dispatch(RegisterIntent.CloseError)


}
package com.android.salamandra.ui.register

import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(): BaseViewModel<RegisterState, RegisterIntent>(RegisterState.initial) {

    override fun reduce(state: RegisterState, intent: RegisterIntent): RegisterState { //This function reduces each intent with a when
        return when(intent){
            is RegisterIntent.Error -> state.copy(
                error = Error(isError = true, errorMsg = intent.errorMsg),
                loading = false
            )

            is RegisterIntent.Loading -> state.copy(
                loading = true
            )
        }
    }
    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onX(){ //Example fun
        dispatch(RegisterIntent.Loading)
    }


}
package com.android.salamandra.ui.splash

import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(): BaseViewModel<SplashState, SplashIntent>(SplashState.initial) {

    override fun reduce(state: SplashState, intent: SplashIntent): SplashState { //This function reduces each intent with a when
        return when(intent){
            is SplashIntent.Error -> state.copy(
                error = Error(isError = true, errorMsg = intent.errorMsg),
                loading = false
            )

            is SplashIntent.Loading -> state.copy(
                loading = true
            )
        }
    }
    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onX(){ //Example fun
        dispatch(SplashIntent.Loading)
    }


}
package com.android.salamandra.ui.splash

import com.android.salamandra.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor() :
    BaseViewModel<SplashState, SplashIntent>(SplashState.initial) {

    override fun reduce(intent: SplashIntent) { //This function reduces each intent with a when
        state = when (intent) {
            is SplashIntent.Error -> state.copy(error = intent.error)

            is SplashIntent.Loading -> state.copy(loading = true)
        }
    }
}
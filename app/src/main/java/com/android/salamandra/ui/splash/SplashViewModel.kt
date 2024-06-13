package com.android.salamandra.ui.splash

import com.android.salamandra.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher) :
    BaseViewModel<SplashState, SplashIntent,SplashEvent>(SplashState.initial, ioDispatcher) {

    override fun reduce(intent: SplashIntent) { //This function reduces each intent with a when
        when (intent) {
            is SplashIntent.Error -> _state.update { it.copy(error = intent.error) }

            is SplashIntent.Loading -> _state.update { it.copy(loading = true) }
        }
    }
}
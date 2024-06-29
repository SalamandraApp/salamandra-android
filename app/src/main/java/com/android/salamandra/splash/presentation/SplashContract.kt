package com.android.salamandra.splash.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State


data class SplashState(
    val loading: Boolean,
    val error: UiText?
) : State {
    companion object {
        val initial: SplashState = SplashState(
            loading = false,
            error = null
        )
    }
}


sealed class SplashIntent: Intent {
    data class Loading(val isLoading: Boolean): SplashIntent()
    data class Error(val error: UiText): SplashIntent()
}

sealed class SplashEvent: Event {
    data object NavigateToHome: SplashEvent()
}
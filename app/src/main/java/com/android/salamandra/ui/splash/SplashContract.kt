package com.android.salamandra.ui.splash

import com.android.salamandra.ui.UiText
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


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
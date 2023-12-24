package com.android.salamandra.ui.splash

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class SplashState(
    val loading: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: SplashState = SplashState(
            loading = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class SplashIntent: Intent {
    data object Loading: SplashIntent()
    data class Error(val errorMsg: String): SplashIntent()
}
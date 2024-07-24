package com.android.salamandra.settings.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError


data  class SettingsState(
    val error: RootError?
) : State {
    companion object {
        val initial: SettingsState = SettingsState(
            error = null
        )
    }
}

sealed class SettingsIntent: Intent {
    data class Error(val error: RootError): SettingsIntent()
    data object CloseError: SettingsIntent()
    data object NavigateUp: SettingsIntent()
    data object Logout: SettingsIntent()
}

sealed class SettingsEvent: Event{
    data object NavigateUp: SettingsEvent()
    data object NavigateToHome: SettingsEvent()
}

data class SettingsNavArgs(
    val dummy: Int? = null
): NavArgs
package com.android.salamandra.profile.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra.home.presentation.HomeEvent
import com.android.salamandra.home.presentation.HomeIntent
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec


data class ProfileState(
    val loading: Boolean,
    val error: RootError?
) : State {
    companion object {
        val initial: ProfileState = ProfileState(
            loading = false,
            error = null
        )
    }
}

sealed class ProfileIntent: Intent {
    data class Error(val error: RootError): ProfileIntent()
    data object CloseError: ProfileIntent()
    data class BottomBarClicked(val destination: DirectionDestinationSpec): ProfileIntent()
}

sealed class ProfileEvent: Event{

    data class BottomBarClicked(val destination: DirectionDestinationSpec): ProfileEvent()
}
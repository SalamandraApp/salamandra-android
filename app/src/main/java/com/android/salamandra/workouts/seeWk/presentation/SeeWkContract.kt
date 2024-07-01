package com.android.salamandra.workouts.seeWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError


data class SeeWkState(
    val loading: Boolean,
    val error: RootError?
) : State {
    companion object {
        val initial: SeeWkState = SeeWkState(
            loading = false,
            error = null
        )
    }
}

sealed class SeeWkIntent: Intent {
    data class Error(val error: RootError): SeeWkIntent()
    data object CloseError: SeeWkIntent()
}

sealed class SeeWkEvent: Event{
}

data class SeeWkNavArgs(
    val dummy: Int? = null
): NavArgs

package com.android.salamandra.workouts.executeWk

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError


data class ExecuteWkState(
    val error: RootError?
) : State {
    companion object {
        val initial: ExecuteWkState = ExecuteWkState(
            error = null
        )
    }
}

sealed class ExecuteWkIntent: Intent {
    data class Error(val error: RootError): ExecuteWkIntent()
    data object CloseError: ExecuteWkIntent()
}

sealed class ExecuteWkEvent: Event{
}

data class ExecuteWkNavArgs(
    val wkTemplateId: String
): NavArgs
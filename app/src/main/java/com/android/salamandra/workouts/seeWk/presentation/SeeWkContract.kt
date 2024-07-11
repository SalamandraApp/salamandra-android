package com.android.salamandra.workouts.seeWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate


data class SeeWkState(
    val loading: Boolean,
    val wkTemplate: WorkoutTemplate,
    val error: RootError?
) : State {
    companion object {
        val initial: SeeWkState = SeeWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),
        )
    }
}

sealed class SeeWkIntent: Intent {
    data class Error(val error: RootError): SeeWkIntent()
    data object CloseError: SeeWkIntent()
    data object NavigateUp: SeeWkIntent()
}

sealed class SeeWkEvent: Event{
    data object NavigateUp: SeeWkEvent()
}

data class SeeWkNavArgs(
    val wkTemplateId: String
): NavArgs

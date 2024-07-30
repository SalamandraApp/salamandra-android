package com.android.salamandra.workouts.seeWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate


data class SeeWkState(
    val error: RootError?,
    val wkTemplate: WorkoutTemplate,
    val selectedElementIndex: Int?,
) : State {
    companion object {
        val initial: SeeWkState = SeeWkState(
            error = null,
            wkTemplate = WorkoutTemplate(),
            selectedElementIndex = null
        )
    }
}

sealed class SeeWkIntent: Intent {
    data class Error(val error: RootError): SeeWkIntent()

    data object CloseError: SeeWkIntent()

    data object NavigateUp: SeeWkIntent()

    data class ShowBottomSheet(val index: Int): SeeWkIntent()

    data object HideBottomSheet: SeeWkIntent()
}

sealed class SeeWkEvent: Event{
    data object NavigateUp: SeeWkEvent()
}

data class SeeWkNavArgs(
    val wkTemplateId: String
): NavArgs

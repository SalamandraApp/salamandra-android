package com.android.salamandra.workouts.editWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate


data class EditWkState(
    val loading: Boolean,
    val error: UiText?,
    val wkTemplate: WorkoutTemplate
) : State {
    companion object {
        val initial: EditWkState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate()
        )
    }
}

sealed class EditWkIntent: Intent {
    data class Error(val error: UiText): EditWkIntent()
    data object CloseError: EditWkIntent()
    data class ChangeWkName(val newName: String): EditWkIntent()
    data class ChangeWkDescription(val newDescription: String): EditWkIntent()
}

sealed class EditWkEvent: Event{
}

data class EditWkNavArgs(
    val dummy: Int? = null
): NavArgs
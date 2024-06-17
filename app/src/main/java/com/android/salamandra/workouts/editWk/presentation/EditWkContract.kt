package com.android.salamandra.workouts.editWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate


data class EditWkState(
    val loading: Boolean,
    val error: RootError?,
    val wkTemplate: WorkoutTemplate,
    val showSearchExercise: Boolean,
    val searchTerm: String,
    val exerciseList: List<Exercise>
) : State {
    companion object {
        val initial: EditWkState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),
            showSearchExercise = false,
            searchTerm = "",
            exerciseList = emptyList()
        )
    }
}

sealed class EditWkIntent: Intent {
    data class Error(val error: RootError): EditWkIntent()
    data object CloseError: EditWkIntent()
    data class ChangeWkName(val newName: String): EditWkIntent()
    data class ChangeWkDescription(val newDescription: String): EditWkIntent()
    data class ShowSearchExercise(val show: Boolean): EditWkIntent()
    data class ChangeSearchTerm(val newTerm: String): EditWkIntent()
    data object SearchExercise: EditWkIntent()
}

sealed class EditWkEvent: Event{
}

data class EditWkNavArgs(
    val dummy: Int? = null
): NavArgs
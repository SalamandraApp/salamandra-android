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
    val bottomSheet: Boolean,
    val selectedExercise: Exercise?
) : State {
    companion object {
        val initial: EditWkState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),

            bottomSheet = false,
            selectedExercise = null,
        )
    }
}

sealed class EditWkIntent: Intent {
    data class Error(val error: RootError): EditWkIntent()
    data object CloseError: EditWkIntent()
    data object NavigateToHome: EditWkIntent()
    data object NavigateToSearch: EditWkIntent()

    data class ShowBottomSheet(val exercise: Exercise): EditWkIntent()
    data object HideBottomSheet: EditWkIntent()

    data class ChangeWkName(val newName: String): EditWkIntent()
    data class ChangeWkDescription(val newDescription: String): EditWkIntent()

    data class ChangeWkElementSets(val newSets: Int, val index: Int): EditWkIntent()
    data class ChangeWkElementReps(val newReps: Int, val index: Int): EditWkIntent()
    data class ChangeWkElementWeight(val newWeight: Double, val index: Int): EditWkIntent()

    data object CreteWorkout: EditWkIntent()
}

sealed class EditWkEvent: Event{
    data object NavigateToHome: EditWkEvent()
    data object NavigateToSearch: EditWkEvent()
}

data class EditWkNavArgs(
    val addedExercises: Array<String> = emptyArray()
): NavArgs
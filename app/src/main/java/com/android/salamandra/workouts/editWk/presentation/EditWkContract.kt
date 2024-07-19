package com.android.salamandra.workouts.editWk.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate


data class EditWkState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val loading: Boolean,
    val error: RootError?,
    val wkTemplate: WorkoutTemplate,
    val selectedElementIndex: Int?,
) : State {
    companion object {
        val initial: EditWkState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),

            selectedElementIndex = null,
        )
    }
}

sealed class EditWkIntent: Intent {
    data class Error(val error: RootError): EditWkIntent()
    data object CloseError: EditWkIntent()
    data object NavigateToHome: EditWkIntent()
    data object NavigateToSearch: EditWkIntent()

    data class ShowBottomSheet(val index: Int): EditWkIntent()
    data object HideBottomSheet: EditWkIntent()

    data class ChangeWkName(val newName: String): EditWkIntent()
    data class ChangeWkDescription(val newDescription: String): EditWkIntent()

    data class ChangeSets(val newSets: Int, val index: Int): EditWkIntent()
    data class ChangeReps(val newReps: Int, val index: Int): EditWkIntent()
    data class ChangeWeight(val newWeight: Double, val index: Int): EditWkIntent()
    data class ChangeRest(val newRest: Int, val index: Int): EditWkIntent()
    data class DeleteWkElement(val index: Int): EditWkIntent()

    data object CreateWorkout: EditWkIntent()
}

sealed class EditWkEvent: Event{
    data object NavigateToHome: EditWkEvent()
    data object NavigateToSearch: EditWkEvent()
}

data class EditWkNavArgs(
    val addedExercises: Array<String> = emptyArray()
): NavArgs
package com.android.salamandra.workouts.editWk.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.focus.FocusRequester
import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate


data class EditWkState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val loading: Boolean,
    val error: RootError?,
    val wkTemplate: WorkoutTemplate,
    val selectedElementIndex: Int?,
    val textFieldSelected: Int? = null,
    val notImplementedBanner: Boolean,
) : State {
    companion object {
        val initial: EditWkState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),
            selectedElementIndex = null,
            notImplementedBanner = false
        )
    }
}

sealed class EditWkIntent: Intent {
    data class Error(val error: RootError): EditWkIntent()
    data object CloseError: EditWkIntent()

    data object NavigateToHome: EditWkIntent()
    data object NavigateToSearch: EditWkIntent()

    data class ShowElementBanner(val index: Int, val field: Int): EditWkIntent()
    data object ShowNotImplementedBanner: EditWkIntent()
    data object HideBottomSheet: EditWkIntent()

    data class ChangeWkName(val newName: String): EditWkIntent()
    data class ChangeWkDescription(val newDescription: String): EditWkIntent()
    data class ChangeSets(val newSets: Int): EditWkIntent()
    data class ChangeReps(val newReps: Int): EditWkIntent()
    data class ChangeWeight(val newWeight: Double): EditWkIntent()
    data class ChangeRest(val newRest: Int): EditWkIntent()

    data object DeleteWkElement: EditWkIntent()
    data object CreateWorkout: EditWkIntent()
}

sealed class EditWkEvent: Event{
    data object NavigateToHome: EditWkEvent()

    data object NavigateToSearch: EditWkEvent()
}

data class EditWkNavArgs(
    val addedExercises: Array<String> = emptyArray()
): NavArgs
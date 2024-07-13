package com.android.salamandra.workouts.editWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.workouts.seeWk.presentation.SeeWkIntent


data class EditWkState(
    val loading: Boolean,
    val error: RootError?,
    val wkTemplate: WorkoutTemplate,
    val showSearchExercise: Boolean,
    val searchTerm: String,
    val exerciseList: List<Exercise>,
    val bottomSheet: Boolean,
    val exerciseSelectedIndex: Int?
) : State {
    companion object {
        val initial: EditWkState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),
            showSearchExercise = false,
            searchTerm = "",
            exerciseList = emptyList(),
            bottomSheet = false,
            exerciseSelectedIndex = null,
        )
    }
}

sealed class EditWkIntent: Intent {
    data class Error(val error: RootError): EditWkIntent()
    data object CloseError: EditWkIntent()
    data object NavigateUp: EditWkIntent()

    data class ShowBottomSheet(val index: Int): EditWkIntent()
    data object HideBottomSheet: EditWkIntent()

    data class ShowSearchExercise(val show: Boolean): EditWkIntent()
    data class ChangeSearchTerm(val newTerm: String): EditWkIntent()
    data object SearchExercise: EditWkIntent()
    data class AddExerciseToTemplate(val exercise: Exercise): EditWkIntent()

    data class ChangeWkName(val newName: String): EditWkIntent()
    data class ChangeWkDescription(val newDescription: String): EditWkIntent()
    data class ChangeWkElementSets(val newSets: Int, val index: Int): EditWkIntent()
    data class ChangeWkElementReps(val newReps: Int, val index: Int): EditWkIntent()
    data class ChangeWkElementWeight(val newWeight: Double, val index: Int): EditWkIntent()
}

sealed class EditWkEvent: Event{
    data object NavigateUp: EditWkEvent()
}

data class EditWkNavArgs(
    val dummy: Int? = null
): NavArgs
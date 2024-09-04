package com.android.salamandra.workouts.executeWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionExercise
import com.android.salamandra.workouts.executeWk.presentation.components.ExecuteWkScreenDestinations


data class ExecuteWkState(
    val error: RootError?,
    val exerciseList: List<WkExecutionExercise>,
    val indexOfCurrentExercise: Int,
    val currentSet: Int,
    val workoutEnded: Boolean,
    val survey: Int?,
    val startOfSetCurrentTimeMillis: Long,
    val indexOfSelectedElement: Int?,
    val workoutTemplateId: String,
    val activeTab: ExecuteWkScreenDestinations
) : State {
    companion object {
        val initial: ExecuteWkState = ExecuteWkState(
            error = null,
            exerciseList = emptyList(),
            indexOfCurrentExercise = 0,
            currentSet = 1,
            workoutEnded = false,
            survey = null,
            startOfSetCurrentTimeMillis = 0,
            indexOfSelectedElement = null,
            workoutTemplateId = "",
            activeTab = ExecuteWkScreenDestinations.ExecuteScreen
        )
    }
}

sealed class ExecuteWkIntent : Intent {
    data class Error(val error: RootError) : ExecuteWkIntent()

    data object CloseError : ExecuteWkIntent()

    data object LogAction : ExecuteWkIntent()

    data object ChangeSurveyToSad : ExecuteWkIntent()

    data object ChangeSurveyToNeutral : ExecuteWkIntent()

    data object ChangeSurveyToHappy : ExecuteWkIntent()

    data object SkipSet : ExecuteWkIntent()

    data object EndWorkout : ExecuteWkIntent()

    data object HideBottomSheet : ExecuteWkIntent()

    data class ShowBottomSheet(val setNumber: Int) : ExecuteWkIntent()

    data class EditReps(val newReps: Int) : ExecuteWkIntent()

    data class EditWeight(val newWeight: Double) : ExecuteWkIntent()

    data class EditRest(val newRest: Int) : ExecuteWkIntent()

    data class ChangeActiveTab(val newTab: ExecuteWkScreenDestinations) : ExecuteWkIntent()
}

sealed class ExecuteWkEvent : Event {
    data object EndWorkout : ExecuteWkEvent()
}

data class ExecuteWkNavArgs(
    val wkTemplateId: String
) : NavArgs
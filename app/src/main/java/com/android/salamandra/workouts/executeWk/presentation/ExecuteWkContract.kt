package com.android.salamandra.workouts.executeWk.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionExercise
import com.android.salamandra.workouts.executeWk.presentation.components.ExecuteWkScreenDestinations


data class ExecuteWkState(
    val scaffoldTab: ExecuteWkScreenDestinations,
    val error: RootError?,
    val loading: Boolean,

    val workoutTemplateId: String,
    val exerciseList: List<WkExecutionExercise>,
    val currExercise: Int,
    val currSet: Int,

    val finishedExecution: Boolean = false,
    val pausedExecution: Boolean = false,
    val survey: Int?,
    val startOfSetCurrentTimeMillis: Long,

    val selectedElement: Int?,
    val textFieldSelected: Int? = null,
) : State {
    companion object {
        val initial: ExecuteWkState = ExecuteWkState(
            error = null,
            exerciseList = emptyList(),
            currExercise = 0,
            loading = true,
            currSet = 0,
            finishedExecution = false,
            survey = null,
            startOfSetCurrentTimeMillis = 0,
            selectedElement = null,
            workoutTemplateId = "",
            scaffoldTab = ExecuteWkScreenDestinations.ExecuteScreen
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
    data object DiscardWorkout: ExecuteWkIntent()
    data object EndWorkoutEarly: ExecuteWkIntent()

    data object HideBottomSheet : ExecuteWkIntent()
    data class ShowBottomSheet(val setNumber: Int, val fieldSelected: Int) : ExecuteWkIntent()

    data object StopWorkout: ExecuteWkIntent()
    data object ContinueWorkout: ExecuteWkIntent()

    data class EditReps(val newReps: Int) : ExecuteWkIntent()
    data class EditWeight(val newWeight: Double) : ExecuteWkIntent()
    data class EditRest(val newRest: Int) : ExecuteWkIntent()
    data object AddRest : ExecuteWkIntent()

    data class ChangeActiveTab(val newTab: ExecuteWkScreenDestinations) : ExecuteWkIntent()
}

sealed class ExecuteWkEvent : Event {
    data object EndWorkout : ExecuteWkEvent()
}

data class ExecuteWkNavArgs(
    val wkTemplateId: String
) : NavArgs
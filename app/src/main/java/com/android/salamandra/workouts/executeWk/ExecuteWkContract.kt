package com.android.salamandra.workouts.executeWk

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionExercise


data class ExecuteWkState(
    val error: RootError?,
    val executionExercises: List<WkExecutionExercise>,
    val currentExercise: WkExecutionExercise?,
    val currentSet: Int
) : State {
    companion object {
        val initial: ExecuteWkState = ExecuteWkState(
            error = null,
            executionExercises = emptyList(),
            currentExercise = null,
            currentSet = 1
        )
    }
}

sealed class ExecuteWkIntent: Intent {
    data class Error(val error: RootError): ExecuteWkIntent()
    data object CloseError: ExecuteWkIntent()
    data object LogAction: ExecuteWkIntent()
}

sealed class ExecuteWkEvent: Event{
    data object EndWorkout: ExecuteWkEvent()
}

data class ExecuteWkNavArgs(
    val wkTemplateId: String
): NavArgs
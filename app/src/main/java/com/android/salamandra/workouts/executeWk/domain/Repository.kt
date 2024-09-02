package com.android.salamandra.workouts.executeWk.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.executions.WorkoutExecution
import com.android.salamandra._core.domain.model.workout.template.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate

interface Repository {

    suspend fun createWorkoutExecution(
        wkTemplateId: String,
        workoutExecution: WorkoutExecution
    ): Result<Unit, DataError>
}
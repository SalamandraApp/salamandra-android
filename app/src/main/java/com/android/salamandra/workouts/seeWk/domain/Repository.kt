package com.android.salamandra.workouts.seeWk.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate

interface Repository {
    suspend fun getWkTemplate(workoutId: String): Result<WorkoutTemplate, DataError>
}
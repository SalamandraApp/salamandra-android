package com.android.salamandra.workouts.commons.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate

interface WorkoutsRepository {
    suspend fun storeWkTemplateInLocal(wkTemplate: WorkoutTemplate)
    suspend fun getWkTemplate(workoutId: String): Result<WorkoutTemplate, DataError>
}
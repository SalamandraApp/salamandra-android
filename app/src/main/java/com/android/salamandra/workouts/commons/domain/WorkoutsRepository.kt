package com.android.salamandra.workouts.commons.domain

import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate

interface WorkoutsRepository {
    suspend fun storeWkTemplateInLocal(wkTemplate: WorkoutTemplate)
}
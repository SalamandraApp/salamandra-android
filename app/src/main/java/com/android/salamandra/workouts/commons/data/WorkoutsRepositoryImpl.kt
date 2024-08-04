package com.android.salamandra.workouts.commons.data

import android.util.Log
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository

class WorkoutsRepositoryImpl(
    private val localDbRepository: LocalDbRepository
): WorkoutsRepository {

    override suspend fun storeWkTemplateInLocal(wkTemplate: WorkoutTemplate)  = localDbRepository.insertWkTemplate(wkTemplate)

}
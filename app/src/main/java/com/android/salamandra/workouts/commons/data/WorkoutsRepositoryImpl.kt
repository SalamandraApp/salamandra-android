package com.android.salamandra.workouts.commons.data

import android.util.Log
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository

class WorkoutsRepositoryImpl(
    private val localDbRepository: LocalDbRepository
): WorkoutsRepository {

    override suspend fun storeWkTemplateInLocal(wkTemplate: WorkoutTemplate) {
        when (val insertion = localDbRepository.insertWkTemplate(wkTemplate)){
            is Result.Success -> Log.i("SLM", "WorkoutTemplate insertion completed successfully")
            is Result.Error -> Log.e("SLM", "Error occurred while inserting Workout template in local: ${insertion.error}")
        }

    }

}
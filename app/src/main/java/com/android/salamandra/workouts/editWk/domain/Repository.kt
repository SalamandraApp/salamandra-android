package com.android.salamandra.workouts.editWk.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise

interface Repository {
    suspend fun getAllExercises(exerciseIdList: Array<String>): List<Exercise>
}
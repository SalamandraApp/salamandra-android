package com.android.salamandra.workouts.searchExercise.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise

interface Repository {
    suspend fun getExercises(term: String): Result<List<Exercise>, DataError.Network>
}
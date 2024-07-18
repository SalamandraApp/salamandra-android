package com.android.salamandra.workouts.search.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise

interface Repository {
    suspend fun getExercises(term: String): Result<List<Exercise>, DataError.Network>
    suspend fun insertExerciseInLocal(exercise: Exercise): Result<Unit, DataError.Local>
}
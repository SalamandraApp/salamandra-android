package com.android.salamandra.home.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise

interface Repository {
    suspend fun getExercise(term: String): List<Exercise>?

    suspend fun logout(): Result<Unit, DataError.Cognito>
}
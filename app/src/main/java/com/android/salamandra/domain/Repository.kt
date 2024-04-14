package com.android.salamandra.domain

import com.android.salamandra.domain.error.DataError
import com.android.salamandra.domain.error.Result
import com.android.salamandra.domain.model.ExerciseModel

interface Repository {
    suspend fun login(email: String, password: String): Result<Unit, DataError.Cognito>
    suspend fun register(email: String, password: String, username: String, onSuccess: () -> Unit)

    suspend fun confirmRegister(username: String, code: String, onSuccess: () -> Unit)

    suspend fun getExercise(term: String): List<ExerciseModel>?

    suspend fun logout()
}
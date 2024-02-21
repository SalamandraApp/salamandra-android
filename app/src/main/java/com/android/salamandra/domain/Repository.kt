package com.android.salamandra.domain

import com.android.salamandra.domain.model.ExerciseModel
import com.android.salamandra.domain.model.UserModel

interface Repository {
    suspend fun login(email: String, password: String): Result<UserModel>

    suspend fun register(email: String, password: String, username: String): Boolean

    suspend fun confirmRegister(username: String, code: String): Boolean

    suspend fun getExercise(term: String): List<ExerciseModel>?
}
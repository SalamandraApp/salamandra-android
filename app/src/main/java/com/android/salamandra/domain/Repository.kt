package com.android.salamandra.domain

import com.android.salamandra.domain.model.ExerciseModel

interface Repository {
    suspend fun login(email: String, password: String)

    suspend fun register(email: String, password: String, username: String)

    suspend fun confirmRegister(username: String, code: String)

    suspend fun getExercise(term: String): List<ExerciseModel>?
}
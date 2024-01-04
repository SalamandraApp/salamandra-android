package com.android.salamandra.domain

import com.android.salamandra.domain.model.ExerciseModel

interface Repository {
    suspend fun login(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit)

    suspend fun register(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit)

    suspend fun getExercise(term: String): List<ExerciseModel>?
}
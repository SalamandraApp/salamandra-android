package com.android.salamandra.settings.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun logout(): Result<Unit, DataError>
}
package com.android.salamandra.home.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.template.WorkoutPreview
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getWkPreviews(): Flow<List<WorkoutPreview>>
    suspend fun isLocalDbEmpty(): Boolean
    suspend fun getWkPreviewsFromRemoteAndStoreInLocal(): Result<Unit, DataError>
}
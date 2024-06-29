package com.android.salamandra.splash.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutPreview


interface Repository {
    suspend fun isLocalDbEmpty(): Boolean
    suspend fun getWkPreviewsFromRemoteAndStoreInLocal(): Result<Unit, DataError>
}
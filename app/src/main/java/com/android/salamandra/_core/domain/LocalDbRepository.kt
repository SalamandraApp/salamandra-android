package com.android.salamandra._core.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import kotlinx.coroutines.flow.Flow
import user.UserEntity
import workout.WorkoutTemplateEntity

interface LocalDbRepository {
    suspend fun isWkTemplateEntityEmpty(): Boolean
    suspend fun insertWkPreviewList(wkPreviewList: List<WorkoutPreview>): Result<Unit, DataError.Local>
    fun getAllWkPreviews(): Flow<List<WorkoutPreview>>
    suspend fun insertUser(user: User): Result<Unit, DataError.Local>
    suspend fun getUserByID(id: String): Result<UserEntity, DataError.Local>
    suspend fun clearAllDatabase()
    suspend fun getWkPreviewByID(id: String): Result<WorkoutTemplateEntity, DataError.Local>
    suspend fun insertWkTemplate(wkTemplate: WorkoutTemplate): Result<Unit, DataError.Local>
    suspend fun getWkTemplate(wkId: String): Result<WorkoutTemplate, DataError.Local>
}
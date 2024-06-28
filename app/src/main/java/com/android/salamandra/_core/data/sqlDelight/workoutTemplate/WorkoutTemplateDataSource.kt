package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import workout.WorkoutTemplateEntity
import java.util.Date

interface WorkoutTemplateDataSource {
    suspend fun getWkByID(id: String): Result<WorkoutTemplateEntity, DataError.Local>

    fun getAllWk(): Flow<List<WorkoutTemplateEntity>>

    suspend fun deleteWkByID(id: String): Result<Unit, DataError.Local>

    suspend fun insertWk(
        id: String,
        name: String,
        description: String,
        dateCreated: Date
    ): Result<Unit, DataError.Local>

    suspend fun clearDatabase(): Result<Unit, DataError.Local>
}
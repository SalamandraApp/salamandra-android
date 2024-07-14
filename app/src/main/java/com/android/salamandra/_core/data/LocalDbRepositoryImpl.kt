package com.android.salamandra._core.data

import com.android.salamandra._core.data.sqlDelight.user.UserDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import user.UserEntity
import workout.WorkoutTemplateEntity
import javax.inject.Inject

class LocalDbRepositoryImpl @Inject constructor(
    private val workoutTemplateDataSource: WorkoutTemplateDataSource,
    private val userDataSource: UserDataSource
) : LocalDbRepository {
    //Workout template
    override suspend fun isWkTemplateEntityEmpty(): Boolean =
        workoutTemplateDataSource.isWkTemplateEntityEmpty()

    override suspend fun insertWkPreviewList(wkPreviewList: List<WorkoutPreview>): Result<Unit, DataError.Local> =
        workoutTemplateDataSource.insertWkPreviewList(wkPreviewList)

    override fun getAllWkPreviews(): Flow<List<WorkoutPreview>> =
        workoutTemplateDataSource.getAllWkPreviews()

    override suspend fun getWkByID(id: String) = workoutTemplateDataSource.getWkByID(id)

    //User
    override suspend fun insertUser(user: User): Result<Unit, DataError.Local> =
        userDataSource.insertUser(user)

    override suspend fun getUserByID(id: String): Result<UserEntity, DataError.Local> =
        userDataSource.getUserByID(id)

    override suspend fun clearAllDatabase() {
        workoutTemplateDataSource.clearDatabase()
        userDataSource.clearDatabase()
    }

}
package com.android.salamandra.home.data

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra.home.domain.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val localDb: LocalDbRepository,
    private val salamandraApiService: SalamandraApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {

    override fun getWkPreviews(): Flow<List<WorkoutPreview>> = localDb.getAllWkPreviews()

    override suspend fun isLocalDbEmpty() = localDb.isWkTemplateEntityEmpty()

    override suspend fun getWkPreviewsFromRemoteAndStoreInLocal(): Result<Unit, DataError> {
        return try {
            when (val uid = dataStoreRepository.getUidFromDatastore()) {
                is Result.Success -> {
                    val wkPreviews = salamandraApiService.getWorkoutPreviews(uid.data)
                    when (val insertionInLocal =
                        localDb.insertWkPreviewList(wkPreviews.toDomain())) {
                        is Result.Success -> Result.Success(Unit)
                        is Result.Error -> Result.Error(insertionInLocal.error)
                    }
                }

                is Result.Error -> Result.Error(uid.error)
            }
        } catch (exception: Exception) {
            Result.Error(retrofitExceptionHandler.handleException(exception))
        }
    }
}
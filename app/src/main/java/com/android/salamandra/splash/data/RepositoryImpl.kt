package com.android.salamandra.splash.data

import android.util.Log
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra.splash.domain.Repository
import retrofit2.HttpException
import java.net.ConnectException

class RepositoryImpl(
    private val workoutTemplateDataSource: WorkoutTemplateDataSource,
    private val salamandraApiService: SalamandraApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {

    override suspend fun isLocalDbEmpty() = workoutTemplateDataSource.isWkTemplateEntityEmpty()

    override suspend fun getWkPreviewsFromRemoteAndStoreInLocal(): Result<Unit, DataError> {
        return try {
            when (val uid = dataStoreRepository.getUidFromDatastore()) {
                is Result.Success -> {
                    val wkPreviews = salamandraApiService.getWorkoutPreviews(uid.data)
                    when (val insertionInLocal =
                        workoutTemplateDataSource.insertWkPreviewList(wkPreviews.toDomain())) {
                        is Result.Success -> Result.Success(Unit)
                        is Result.Error -> Result.Error(insertionInLocal.error)
                    }

                }

                is Result.Error -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
            }
        } catch (exception: Exception) {
            Result.Error(retrofitExceptionHandler.handleException(exception))
        }
    }
}
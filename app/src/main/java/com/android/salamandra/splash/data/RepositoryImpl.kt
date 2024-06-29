package com.android.salamandra.splash.data

import com.android.salamandra._core.data.cognito.CognitoService
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
    private val dataStoreRepository: DataStoreRepository
): Repository {

    override suspend fun isLocalDbEmpty() = workoutTemplateDataSource.isWkTemplateEntityEmpty()

    override suspend fun getWkPreviewsFromRemote(): Result<List<WorkoutPreview>, DataError.Network> {
        return try {
            when (val uid = dataStoreRepository.getUidFromDatastore()){
                is Result.Success -> {
                    val wkPreviews = salamandraApiService.getWorkoutPreviews(uid.data)
                    Result.Success(wkPreviews.toDomain())
                }
                is Result.Error -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
            }
        } catch (httpException: HttpException){
            when(httpException.code()){
                else -> Result.Error(DataError.Network.TOO_MANY_REQUESTS) // TODO Add different types of http exception
            }

        } catch (connectException: ConnectException) {
            Result.Success(emptyList())
        }
    }
}
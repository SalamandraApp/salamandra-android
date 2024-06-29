package com.android.salamandra.splash.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra.splash.domain.Repository
import retrofit2.HttpException

class RepositoryImpl(
    private val workoutTemplateDataSource: WorkoutTemplateDataSource,
    private val salamandraApiService: SalamandraApiService
): Repository {

    override suspend fun isLocalDbEmpty() = workoutTemplateDataSource.isWkTemplateEntityEmpty()

    override suspend fun getWkPreviewsFromRemote(): Result<List<WorkoutPreview>, DataError.Network> {
        return try {
            val wkPreviews = salamandraApiService.getWorkoutPreviews()
            Result.Success(wkPreviews)
        } catch (httpException: HttpException){
            when(httpException.code()){
                else -> Result.Error(DataError.Network.TOO_MANY_REQUESTS) // TODO Add different types of http exception
            }

        }
    }

}
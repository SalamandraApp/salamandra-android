package com.android.salamandra.workouts.executeWk.data

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.network.request.toCreateWorkoutExecutionRequest
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.executions.WorkoutExecution
import com.android.salamandra.workouts.executeWk.domain.Repository

class RepositoryImpl(
    private val salamandraApiService: SalamandraApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {
    override suspend fun createWorkoutExecution(
        wkTemplateId: String,
        workoutExecution: WorkoutExecution
    ): Result<Unit, DataError> {
        return try {
            when (val uid = dataStoreRepository.getUidFromDatastore()) {
                is Result.Success -> {
                    salamandraApiService.createWkExecution(
                        userId = uid.data,
                        wkExecution = workoutExecution.toCreateWorkoutExecutionRequest(wkTemplateId)
                    )
                    Result.Success(Unit)
                }

                is Result.Error -> Result.Error(uid.error)
            }

        } catch (e: Exception) {
            Result.Error(retrofitExceptionHandler.handleException(e))
        }
    }
}
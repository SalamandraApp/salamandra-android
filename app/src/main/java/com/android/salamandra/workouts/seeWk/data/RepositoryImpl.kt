package com.android.salamandra.workouts.seeWk.data

import android.util.Log
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.workouts.seeWk.domain.Repository

class RepositoryImpl(
    private val salamandraApiService: SalamandraApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler,
    private val localDbRepository: LocalDbRepository
) : Repository {

    override suspend fun getWkTemplate(workoutId: String): Result<WorkoutTemplate, DataError> {
        val full = true.toString()
        return try {
            when (val uid = dataStoreRepository.getUidFromDatastore()) {
                is Result.Success -> {
                    when(val localWkTemplate = localDbRepository.getWkPreviewByID(workoutId)){
                        is Result.Success -> {
                            if(localWkTemplate.data.onlyPreviewAvailable){
                                Result.Success(salamandraApiService.getWorkoutById(userId = uid.data, wkId = workoutId, full = full).toDomain())
                            } else {
                                when(val wkTemplate = localDbRepository.getWkTemplate(workoutId)){
                                    is Result.Success -> Result.Success(wkTemplate.data)
                                    is Result.Error -> Result.Error(wkTemplate.error)
                                }
                            }
                        }
                        is Result.Error -> {
                            Log.e("SLM", "${localWkTemplate.error}")
                            Result.Success(salamandraApiService.getWorkoutById(userId = uid.data, wkId = workoutId, full = full).toDomain())
                        }
                    }
                }

                is Result.Error -> Result.Error(DataError.Datastore.UID_NOT_FOUND)
            }
        } catch (e: Exception) {
            Result.Error(retrofitExceptionHandler.handleException(e))
        }
    }

}
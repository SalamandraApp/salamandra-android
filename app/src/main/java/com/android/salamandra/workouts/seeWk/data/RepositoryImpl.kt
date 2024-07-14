package com.android.salamandra.workouts.seeWk.data

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
        return try {
            when (val uid = dataStoreRepository.getUidFromDatastore()) {
                is Result.Success -> {
                    when(val localWkTemplate = localDbRepository.getWkByID(workoutId)){
                        is Result.Success -> {
                            if(localWkTemplate.data.onlyPreviewAvailable){
                                Result.Success(salamandraApiService.getWorkoutById(uid.data, workoutId).toDomain()                                )
                            } else {
                                TODO() //TODO get all wk template elements
                            }
                        }
                        is Result.Error -> { //TODO if local error call remote
                            Result.Error(localWkTemplate.error)
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
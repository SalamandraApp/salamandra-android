package com.android.salamandra.workouts.search.data

import android.util.Log
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra.workouts.search.domain.Repository

class RepositoryImpl(
    private val salamandraApiService: SalamandraApiService,
    private val localDbRepository: LocalDbRepository
): Repository {
    override suspend fun getExercises(term: String): Result<List<Exercise>, DataError.Network> {
        runCatching {
            salamandraApiService.searchExercise(term)
        }
            .onSuccess {
                return Result.Success(it.toDomain() ?: emptyList())
            }
            .onFailure {
                Log.i("SLM", "An error occurred while using apiService, ${it.message}")
            }
        return Result.Error(DataError.Network.UNKNOWN)
    }

    override suspend fun insertExerciseInLocal(exercise: Exercise): Result<Unit, DataError.Local> = localDbRepository.insertExercise(exercise)

}
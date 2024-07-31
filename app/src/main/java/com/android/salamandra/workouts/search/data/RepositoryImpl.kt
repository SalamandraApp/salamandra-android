package com.android.salamandra.workouts.search.data

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra.workouts.search.domain.Repository

class RepositoryImpl(
    private val salamandraApiService: SalamandraApiService,
    private val localDbRepository: LocalDbRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {
    override suspend fun getExercises(term: String): Result<List<Exercise>, DataError.Network> {
        return try {
            Result.Success(salamandraApiService.searchExercise(term).toDomain() ?: emptyList())
        } catch (e: Exception) {
            Result.Error(retrofitExceptionHandler.handleException(e))
        }
    }

    override suspend fun insertExerciseInLocal(exercise: Exercise): Result<Unit, DataError.Local> =
        localDbRepository.insertExercise(exercise)

}
package com.android.salamandra.workouts.editWk.data

import android.util.Log
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.network.request.toCreateWorkoutTemplateRequest
import com.android.salamandra._core.data.sqlDelight.exercise.toExercise
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.toWkTemplateElement
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.workouts.editWk.domain.Repository

class RepositoryImpl(
    private val localDbRepository: LocalDbRepository,
    private val salamandraApiService: SalamandraApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {
    companion object {
        const val TEMPORARY_SAVED_ELEMENTS_ID = "TEMPORAL"
    }

    override suspend fun getAllExercises(exerciseIdList: Array<String>): List<Exercise> {
        val exerciseList = mutableListOf<Exercise>()
        exerciseIdList.forEach { id ->
            when (val exercise = localDbRepository.getExerciseByID(id)) {
                is Result.Success -> exerciseList.add(exercise.data.toExercise())
                is Result.Error -> Log.e(
                    "SLM",
                    "Error occurred while getting exercise from local: ${exercise.error}"
                )
            }
        }
        return exerciseList
    }

    override suspend fun createWorkout(workoutTemplate: WorkoutTemplate): Result<Unit, DataError> {
        return try {
            when (val uid = dataStoreRepository.getUidFromDatastore()) {
                is Result.Success -> {
                    val newWorkoutTemplate = salamandraApiService.createWkTemplate(
                        userId = uid.data,
                        wkTemplate = workoutTemplate.toCreateWorkoutTemplateRequest()
                    ).toDomain()

                    when (val insertion = localDbRepository.insertWkTemplate(newWorkoutTemplate)) {
                        is Result.Success -> Result.Success(Unit)
                        is Result.Error -> Result.Error(insertion.error)
                    }
                }

                is Result.Error -> Result.Error(uid.error)
            }

        } catch (e: Exception) {
            Result.Error(retrofitExceptionHandler.handleException(e))
        }
    }

    override suspend fun retrieveSavedWorkoutTemplateElements(): List<WkTemplateElement> {
        return when (val wkTemplateElements =
            localDbRepository.getWkTemplateElementsById(TEMPORARY_SAVED_ELEMENTS_ID)) {
            is Result.Success -> {
                val wkTemplateElementList = wkTemplateElements.data.mapNotNull {
                    when (val exercise = localDbRepository.getExerciseByID(it.exerciseId)) {
                        is Result.Success -> it.toWkTemplateElement(exercise.data.toExercise())
                        is Result.Error -> {
                            Log.e(
                                "SLM",
                                "Error occurred while getting exercise from local: ${exercise.error}"
                            )
                            null
                        }
                    }
                }
                localDbRepository.deleteTemplateElementById(TEMPORARY_SAVED_ELEMENTS_ID)
                return wkTemplateElementList
            }

            is Result.Error -> {
                Log.e("SLM", "Error occurred while retrieving temporal elements")
                emptyList<WkTemplateElement>()
            }
        }
    }

    override suspend fun saveWorkoutTemplateElementsTemporary(wkTemplateElementList: List<WkTemplateElement>) {
        wkTemplateElementList.forEach {
            when (val insertion = localDbRepository.insertWkTemplateElement(
                wkTemplateId = TEMPORARY_SAVED_ELEMENTS_ID,
                wkTemplateElement = it
            )) {
                is Result.Success -> {}
                is Result.Error -> Log.e(
                    "SLM",
                    "An error occurred while storing temporary element: ${insertion.error}"
                )
            }
        }
    }

}
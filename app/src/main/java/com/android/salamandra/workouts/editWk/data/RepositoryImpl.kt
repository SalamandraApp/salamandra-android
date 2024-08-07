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
import java.time.LocalDate
import kotlin.random.Random

class RepositoryImpl(
    private val localDbRepository: LocalDbRepository,
    private val salamandraApiService: SalamandraApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {
    companion object {
        private const val TEMPORARY_SAVED_ELEMENTS_ID = "TEMPORAL"
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
                    val wkTemplateResponse = salamandraApiService.createWkTemplate(
                        userId = uid.data,
                        wkTemplate = workoutTemplate.copy(dateCreated = LocalDate.now())
                            .toCreateWorkoutTemplateRequest()
                    )
                    localDbRepository.insertWkTemplate(
                        wkTemplateResponse.toDomain(workoutTemplate)
                    )
                    Result.Success(Unit)
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
                                "SLM",                                "Error occurred while getting exercise from local: ${exercise.error}"
                            )
                            null
                        }
                    }
                }
                deleteTemporalTemplateElements()
                return wkTemplateElementList
            }

            is Result.Error -> {
                Log.e("SLM", "Error occurred while retrieving temporal elements")
                emptyList()
            }
        }
    }

    override suspend fun deleteTemporalTemplateElements() {
        localDbRepository.deleteTemplateElementById(TEMPORARY_SAVED_ELEMENTS_ID)
    }

    override suspend fun saveWorkoutTemplateElementsTemporarly(wkTemplateElementList: List<WkTemplateElement>) {
        wkTemplateElementList.forEach {
            when (val insertion = localDbRepository.insertWkTemplateElement(
                wkTemplateId = TEMPORARY_SAVED_ELEMENTS_ID,
                wkTemplateElement = it.copy(templateElementId = Random.nextInt().toString())
            )) {
                is Result.Success -> {}
                is Result.Error -> Log.e(
                    "SLM", "An error occurred while storing temporary element: ${insertion.error}"
                )
            }
        }
    }

    override suspend fun getWorkoutTemplateCount(): Int = localDbRepository.countWorkoutTemplateElements()

}
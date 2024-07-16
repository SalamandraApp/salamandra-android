package com.android.salamandra._core.data

import com.android.salamandra._core.data.sqlDelight.exercise.ExerciseDataSource
import com.android.salamandra._core.data.sqlDelight.exercise.toExercise
import com.android.salamandra._core.data.sqlDelight.user.UserDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateElementDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.toWkTemplateElement
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import exercise.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import user.UserEntity
import javax.inject.Inject

class LocalDbRepositoryImpl @Inject constructor(
    private val workoutTemplateDataSource: WorkoutTemplateDataSource,
    private val workoutTemplateElementDataSource: WorkoutTemplateElementDataSource,
    private val exerciseDataSource: ExerciseDataSource,
    private val userDataSource: UserDataSource
) : LocalDbRepository {
    //Workout template
    override suspend fun isWkTemplateEntityEmpty(): Boolean =
        workoutTemplateDataSource.isWkTemplateEntityEmpty()

    override suspend fun insertWkPreviewList(wkPreviewList: List<WorkoutPreview>): Result<Unit, DataError.Local> =
        workoutTemplateDataSource.insertWkPreviewList(wkPreviewList)

    override fun getAllWkPreviews(): Flow<List<WorkoutPreview>> =
        workoutTemplateDataSource.getAllWkPreviews()

    override suspend fun getWkPreviewByID(id: String) = workoutTemplateDataSource.getWkByID(id)

    override suspend fun insertWkTemplate(wkTemplate: WorkoutTemplate): Result<Unit, DataError.Local> { //TODO handle errors
        wkTemplate.elements.forEach { wkTemplateElement ->
            //Exercise
            exerciseDataSource.insertExercise(wkTemplateElement.exercise)
            //Element
            workoutTemplateElementDataSource.insertWkTemplateElement(
                wkTemplateId = wkTemplate.wkId,
                wkTemplateElement = wkTemplateElement,
            )
        }
        //Template
        workoutTemplateDataSource.insertWk(
            id = wkTemplate.wkId,
            name = wkTemplate.name,
            description = wkTemplate.description,
            dateCreated = wkTemplate.dateCreated,
            onlyPreviewAvailable = false
        )
        return Result.Success(Unit)
    }

    override suspend fun getWkTemplate(wkId: String): Result<WorkoutTemplate, DataError.Local> {
        when (val wkTemplateElements =
            workoutTemplateElementDataSource.getWkTemplateElementsById(wkTemplateId = wkId)) {
            is Result.Success -> {
                val wkTemplateElementList: MutableList<WkTemplateElement> = mutableListOf()
                wkTemplateElements.data.forEach { wkTemplateElementEntity ->
                    when (val exerciseEntity =
                        exerciseDataSource.getExerciseByID(wkTemplateElementEntity.exerciseId)) {
                        is Result.Success -> {
                            val wkTemplateElement =
                                wkTemplateElementEntity.toWkTemplateElement(exerciseEntity.data.toExercise())
                            wkTemplateElementList.add(wkTemplateElement)
                        }

                        is Result.Error -> return Result.Error(exerciseEntity.error)
                    }
                }
                return when (val wkTemplateEntity = workoutTemplateDataSource.getWkByID(wkId)) {
                    is Result.Success -> Result.Success(
                        WorkoutTemplate(
                            wkId = wkTemplateEntity.data.id,
                            name = wkTemplateEntity.data.name,
                            elements = wkTemplateElementList,
                            description = wkTemplateEntity.data.description,
                            dateCreated = wkTemplateEntity.data.dateCreated
                        )
                    )

                    is Result.Error -> Result.Error(wkTemplateEntity.error)
                }
            }

            is Result.Error -> return Result.Error(wkTemplateElements.error)
        }
    }

    //User
    override suspend fun insertUser(user: User): Result<Unit, DataError.Local> =
        userDataSource.insertUser(user)

    override suspend fun getUserByID(id: String): Result<UserEntity, DataError.Local> =
        userDataSource.getUserByID(id)

    override suspend fun clearAllDatabase() {
        workoutTemplateDataSource.clearDatabase()
        workoutTemplateElementDataSource.clearDatabase()
        exerciseDataSource.clearDatabase()
        userDataSource.clearDatabase()
    }

    //Exercise
    override suspend fun insertExercise(
        exercise: Exercise
    ): Result<Unit, DataError.Local> = exerciseDataSource.insertExercise(exercise)

    override suspend fun getExerciseByID(id: String): Result<ExerciseEntity, DataError.Local> = exerciseDataSource.getExerciseByID(id)

}
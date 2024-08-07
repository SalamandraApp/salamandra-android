package com.android.salamandra._core.data.sqlDelight.exercise

import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.toEquipment
import com.android.salamandra._core.domain.model.enums.toExerciseType
import com.android.salamandra._core.domain.model.enums.toMuscleGroup
import exercise.ExerciseEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseDataSource @Inject constructor(
    db: SalamandraLocalDB,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val queries = db.exerciseEntityQueries

    suspend fun getExerciseByID(id: String): Result<ExerciseEntity, DataError.Local> {
        return withContext(ioDispatcher) {
            val result = queries.getExerciseById(id).executeAsOneOrNull()
            if (result != null) Result.Success(result)
            else Result.Error(DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND)
        }
    }


    suspend fun insertExercise(
        exercise: Exercise
    ): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.insertExercise(
                id = exercise.exId,
                name = exercise.name,
                mainMuscleGroup = exercise.mainMuscleGroup.ordinal,
                secondaryMuscleGroup = exercise.secondaryMuscleGroup.ordinal,
                necessaryEquipment = exercise.necessaryEquipment.ordinal,
                exerciseType = exercise.exerciseType.ordinal
            )
            Result.Success(Unit)
        }
    }

    suspend fun countElements() = withContext(ioDispatcher) {
        queries.countElements().executeAsOne()
    }

    suspend fun clearDatabase(): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.clearDatabase()
            Result.Success(Unit)
        }
    }
}

fun ExerciseEntity.toExercise(): Exercise {
    return Exercise(
        exId = id,
        name = name,
        mainMuscleGroup = mainMuscleGroup.toMuscleGroup(),
        secondaryMuscleGroup = secondaryMuscleGroup.toMuscleGroup(),
        necessaryEquipment = necessaryEquipment.toEquipment(),
        exerciseType = exerciseType.toExerciseType()
    )
}

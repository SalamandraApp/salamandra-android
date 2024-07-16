package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import android.util.Log
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import workout.WorkoutTemplateElementEntity
import workout.WorkoutTemplateEntity
import java.time.LocalDate
import javax.inject.Inject

class WorkoutTemplateElementDataSource @Inject constructor(
    db: SalamandraLocalDB,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val queries = db.workoutTemplateElementEntityQueries

    suspend fun getWkTemplateElementsById(wkTemplateId: String): Result<List<WorkoutTemplateElementEntity>, DataError.Local> {
        return withContext(ioDispatcher) {
            try {
                Result.Success(queries.getAllElementsOfWk(wkTemplateId = wkTemplateId).executeAsList())

            } catch (e: Exception) {
                Log.e("SLM", "${e.message}")
                Result.Error(DataError.Local.WORKOUT_TEMPLATE_ELEMENT_NOT_FOUND)
            }
        }
    }


    suspend fun insertWkTemplateElement(
        wkTemplateId: String,
        wkTemplateElement: WkTemplateElement
    ): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.insertWkTemplateElement(
                id = wkTemplateElement.templateElementId,
                wkTemplateId = wkTemplateId,
                exerciseId = wkTemplateElement.exercise.exId,
                position = wkTemplateElement.position,
                reps = wkTemplateElement.reps,
                sets = wkTemplateElement.sets,
                weight = wkTemplateElement.weight,
                rest = wkTemplateElement.rest
            )
            Result.Success(Unit)
        }
    }

    suspend fun deleteTemplateElementById(wkTemplateId: String){
        withContext(ioDispatcher) { queries.deleteElementsById(wkTemplateId) }
    }

    suspend fun clearDatabase(): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.clearDatabase()
            Result.Success(Unit)
        }
    }

}

fun WorkoutTemplateElementEntity.toWkTemplateElement(exercise: Exercise): WkTemplateElement = WkTemplateElement(
    templateElementId = id,
    exercise = exercise,
    position = position,
    reps = reps,
    sets = sets,
    weight = weight,
    rest = rest
)














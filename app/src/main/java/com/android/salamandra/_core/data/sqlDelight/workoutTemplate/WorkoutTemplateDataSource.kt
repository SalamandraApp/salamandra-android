package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import workout.WorkoutTemplateEntity
import java.time.LocalDate
import javax.inject.Inject

class WorkoutTemplateDataSource @Inject constructor(
    db: SalamandraLocalDB,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val queries = db.workoutTemplateEntityQueries

    suspend fun getWkByID(id: String): Result<WorkoutTemplateEntity, DataError.Local> {
        return withContext(ioDispatcher) {
            val result = queries.getWkById(id).executeAsOneOrNull()
            if (result != null) Result.Success(result)
            else Result.Error(DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND)
        }
    }

    fun getAllWkPreviews(): Flow<List<WorkoutPreview>> =
        queries.getAllWks().asFlow().mapToList(ioDispatcher).map { entityList ->
            entityList.map { it.toWkPreview() }
        }

    suspend fun deleteWkByID(id: String): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.deleteWkById(id)
            Result.Success(Unit)
        }
    }

    suspend fun insertWk(
        id: String,
        name: String,
        description: String?,
        dateCreated: LocalDate?,
        onlyPreviewAvailable: Boolean
    ): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.insertWkTemplate(id, name, description, dateCreated, onlyPreviewAvailable)
            Result.Success(Unit)
        }
    }

    suspend fun clearDatabase(): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.clearDatabase()
            Result.Success(Unit)

        }
    }

    suspend fun isWkTemplateEntityEmpty(): Boolean {
        return withContext(ioDispatcher) {
            val result = queries.countElements().executeAsOneOrNull()
            !(result == null || result > 0)
        }
    }

    suspend fun insertWkPreviewList(wkPreviewList: List<WorkoutPreview>): Result<Unit, DataError.Local> {
        var errorOccurred = false
        wkPreviewList.forEach { wkPreview ->
            when (insertWk(
                id = wkPreview.wkId,
                name = wkPreview.name,
                onlyPreviewAvailable = true,
                description = null,
                dateCreated = null
            )) {
                is Result.Success -> {/*do nothing*/
                }

                is Result.Error -> errorOccurred = true
            }
        }
        return if (!errorOccurred) Result.Success(Unit)
        else Result.Error(DataError.Local.ERROR_INSERTING_WK_TEMPLATES)
    }
}

fun WorkoutTemplateEntity.toWkPreview(): WorkoutPreview =
    WorkoutPreview(wkId = this.id, name = name, onlyPreviewAvailable = onlyPreviewAvailable)











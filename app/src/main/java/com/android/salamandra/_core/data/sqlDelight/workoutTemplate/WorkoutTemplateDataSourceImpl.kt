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
import java.util.Date
import javax.inject.Inject

class WorkoutTemplateDataSourceImpl @Inject constructor(
    db: SalamandraLocalDB,
    private val ioDispatcher: CoroutineDispatcher
) : WorkoutTemplateDataSource {

    private val queries = db.workoutTemplateEntityQueries

    override suspend fun getWkByID(id: String): Result<WorkoutTemplateEntity, DataError.Local> {
        return withContext(ioDispatcher) {
            val result = queries.getWkById(id).executeAsOneOrNull()
            if (result != null) Result.Success(result)
            else Result.Error(DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND)
        }
    }

    override fun getAllWkPreviews(): Flow<List<WorkoutPreview>> =
        queries.getAllWks().asFlow().mapToList(ioDispatcher).map { entityList ->
            entityList.map { it.toWkPreview() }
        }

    override suspend fun deleteWkByID(id: String): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.deleteWkById(id)
            Result.Success(Unit)
        }
    }

    override suspend fun insertWk(
        id: String,
        name: String,
        description: String?,
        dateCreated: Date?,
        onlyPreviewAvailable: Boolean
    ): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.insertWk(id, name, description, dateCreated, onlyPreviewAvailable)
            Result.Success(Unit)
        }
    }

    override suspend fun clearDatabase(): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.clearDatabase()
            Result.Success(Unit)

        }
    }

    override suspend fun isWkTemplateEntityEmpty(): Boolean {
        return withContext(ioDispatcher) {
            val result = queries.countElements().executeAsOneOrNull()
            !(result == null || result > 0)
        }
    }

    override suspend fun insertWkPreviewList(wkPreviewList: List<WorkoutPreview>): Result<Unit, DataError.Local> {
        var errorOccurred = false
        wkPreviewList.forEach { wkPreview ->
            when (val insertion = insertWk(
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
        return if (errorOccurred) Result.Success(Unit)
        else Result.Error(DataError.Local.ERROR_INSERTING_WK_TEMPLATES)
    }
}

fun WorkoutTemplateEntity.toWkPreview(): WorkoutPreview =
    WorkoutPreview(wkId = this.id, name = name, onlyPreviewAvailable = onlyPreviewAvailable)











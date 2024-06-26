package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import workouts.workoutTemplates.WorkoutTemplateEntity
import javax.inject.Inject

class WorkoutTemplateDataSourceImpl @Inject constructor(db: SalamandraLocalDB, private val ioDispatcher: CoroutineDispatcher): WorkoutTemplateDataSource {

    private val queries = db.workoutTemplatesQueries

    override suspend fun getWkByID(id: String): Result<WorkoutTemplateEntity, DataError.Local> {
        return withContext(ioDispatcher){
            val result = queries.getWkById(id).executeAsOneOrNull()
            if(result != null) Result.Success(result)
            else Result.Error(DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND)
        }
    }

    override fun getAllWk(): Flow<List<WorkoutTemplateEntity>> = queries.getAllWks().asFlow().mapToList(ioDispatcher)

    override suspend fun deleteWkByID(id: String): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher){
            queries.deleteWkById(id)
            Result.Success(Unit)
        }
    }

    override suspend fun insertWkByID(
        id: String,
        name: String,
        description: String
    ): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher){
            queries.insertWk(id, name, description)
            Result.Success(Unit)
        }
    }
}
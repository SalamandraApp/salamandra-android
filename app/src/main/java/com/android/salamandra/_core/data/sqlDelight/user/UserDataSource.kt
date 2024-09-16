package com.android.salamandra._core.data.sqlDelight.user

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.toWkPreview
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.toFitnessGoal
import com.android.salamandra._core.domain.model.enums.toFitnessLevel
import com.android.salamandra._core.domain.model.enums.toGender
import com.android.salamandra._core.domain.model.workout.template.WorkoutPreview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import user.UserEntity
import java.time.LocalDate
import javax.inject.Inject

class UserDataSource @Inject constructor(
    db: SalamandraLocalDB,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val queries = db.userEntityQueries

    suspend fun getUserByID(id: String): Result<UserEntity, DataError.Local> {
        return withContext(ioDispatcher) {
            val result = queries.getUserById(id).executeAsOneOrNull()
            if (result != null) Result.Success(result)
            else Result.Error(DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND)
        }
    }

    fun getUserByIdAsFlow(id: String): Flow<User?> =
        queries.getUserById(id).asFlow().mapToOneOrNull(ioDispatcher).map { it?.toUser() }

    suspend fun deleteUserByID(id: String): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.deleteUserById(id)
            Result.Success(Unit)
        }
    }

    suspend fun insertUser(
        user: User
    ): Result<Unit, DataError.Local> {
        return withContext(ioDispatcher) {
            queries.insertUser(
                id = user.uid,
                username = user.username,
                displayName = user.displayName,
                dateJoined = user.dateJoined,
                dateOfBirth = user.dateOfBirth,
                height = user.height,
                weight = user.weight,
                gender = user.gender?.ordinal,
                fitnessGoal = user.fitnessGoal?.ordinal,
                fitnessLevel = user.fitnessLevel?.ordinal
            )
            Result.Success(Unit)
        }
    }

    suspend fun updateDisplayName(userId: String, newName: String?) = withContext(ioDispatcher) {
        queries.updateDisplayName(newDisplayName = newName, userId = userId)
    }
    suspend fun updateDateOfBirth(userId: String, newDateOfBirth: LocalDate?) = withContext(ioDispatcher) {
        queries.updateDateOfBirth(newDateOfBirth = newDateOfBirth, userId = userId)
    }
    suspend fun updateWeight(userId: String, newWeight: Double?) = withContext(ioDispatcher) {
        queries.updateWeight(newWeight = newWeight, userId = userId)
    }
    suspend fun updateFitnessLevel(userId: String, newFitnessLevel: FitnessLevel?) = withContext(ioDispatcher) {
        queries.updateFitnessLevel(newFitnessLevel = newFitnessLevel?.ordinal, userId = userId)
    }
    suspend fun updateFitnessGoal(userId: String, newFitnessGoal: FitnessGoal?) = withContext(ioDispatcher) {
        queries.updateFitnessGoal(newFitnessGoal = newFitnessGoal?.ordinal, userId = userId)
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

fun UserEntity.toUser(): User {
    return User(
        uid = id,
        username = username,
        displayName = displayName,
        dateJoined = dateJoined,
        dateOfBirth = dateOfBirth,
        height = height,
        weight = weight,
        gender = gender?.toGender(),
        fitnessGoal = fitnessGoal?.toFitnessGoal(),
        fitnessLevel = fitnessLevel?.toFitnessLevel()
    )
}

package com.android.salamandra._core.data.sqlDelight.user

import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.toFitnessGoal
import com.android.salamandra._core.domain.model.enums.toFitnessLevel
import com.android.salamandra._core.domain.model.enums.toGender
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import user.UserEntity
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

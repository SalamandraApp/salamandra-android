package com.android.salamandra.profile.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.network.request.ModifyUserDataRequest
import com.android.salamandra._core.data.sqlDelight.user.toUser
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra.profile.domain.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val localDbRepository: LocalDbRepository,
    private val retrofitExceptionHandler: RetrofitExceptionHandler,
    private val salamandraApiService: SalamandraApiService
) : Repository {
    override suspend fun getUserDataAsFlow(): Flow<User?>? {
        return when (val userId = dataStoreRepository.getUidFromDatastore()) {
            is Result.Success -> localDbRepository.getUserByIdAsFlow(userId.data)
            is Result.Error -> null
        }
    }

    override suspend fun changeWeight(newWeight: Double?): Result<Unit, DataError> {
        return when (val userId = dataStoreRepository.getUidFromDatastore()) {
            is Result.Success -> {
                localDbRepository.updateWeight(userId = userId.data, newWeight = newWeight)
                try {
                    salamandraApiService.updateUserProfile(
                        userId = userId.data,
                        uncompletedUserData = ModifyUserDataRequest(weight = newWeight?.toFloat())
                    )
                    Result.Success(Unit)
                } catch (e: Exception) {
                    Result.Error(retrofitExceptionHandler.handleException(e))
                }
            }

            is Result.Error -> Result.Error(userId.error)
        }
    }

    override suspend fun changeFitnessLevel(newFitnessLevel: FitnessLevel?): Result<Unit, DataError> {
        return when (val userId = dataStoreRepository.getUidFromDatastore()) {
            is Result.Success -> {
                try {
                    localDbRepository.updateFitnessLevel(
                        userId = userId.data,
                        newFitnessLevel = newFitnessLevel
                    )
                    Result.Success(Unit)
                } catch (e: Exception) {
                    Result.Error(DataError.Local.UPDATING_USER_DATA_FAILED)
                }
            }

            is Result.Error -> Result.Error(userId.error)
        }
    }

    override suspend fun changeFitnessGoal(newFitnessGoal: FitnessGoal?): Result<Unit, DataError> {
        return when (val userId = dataStoreRepository.getUidFromDatastore()) {
            is Result.Success -> {
                try {
                    localDbRepository.updateFitnessGoal(
                        userId = userId.data,
                        newFitnessGoal = newFitnessGoal
                    )
                    Result.Success(Unit)
                } catch (e: Exception) {
                    Result.Error(DataError.Local.UPDATING_USER_DATA_FAILED)
                }
            }

            is Result.Error -> Result.Error(userId.error)
        }
    }
}
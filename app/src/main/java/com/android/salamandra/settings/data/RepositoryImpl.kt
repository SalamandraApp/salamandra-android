package com.android.salamandra.settings.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.network.request.ModifyUserDataRequest
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.settings.domain.Repository
import java.time.LocalDate

class RepositoryImpl(
    private val cognitoService: CognitoService,
    private val localDbRepository: LocalDbRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val salamandraApiService: SalamandraApiService,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {

    override suspend fun logout(): Result<Unit, DataError> {
        localDbRepository.clearAllDatabase()
        return cognitoService.logout()
    }

    override suspend fun changeDisplayName(newName: String?): Result<Unit, DataError> {
        return when (val userId = dataStoreRepository.getUidFromDatastore()) {
            is Result.Success -> {
                try {
                    salamandraApiService.updateUserProfile(
                        userId = userId.data,
                        uncompletedUserData = ModifyUserDataRequest(displayName = newName)
                    )
                    localDbRepository.updateDisplayName(userId = userId.data, newName = newName)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    Result.Error(retrofitExceptionHandler.handleException(e))
                }
            }

            is Result.Error -> Result.Error(userId.error)
        }
    }

    override suspend fun changeDateOfBirth(newDateOfBirth: LocalDate?): Result<Unit, DataError> {
        return when (val userId = dataStoreRepository.getUidFromDatastore()) {
            is Result.Success -> {
                try {
                    salamandraApiService.updateUserProfile(
                        userId = userId.data,
                        uncompletedUserData = ModifyUserDataRequest(dateOfBirth = newDateOfBirth?.toString())
                    )
                    localDbRepository.updateDateOfBirth(userId = userId.data, newDateOfBirth = newDateOfBirth)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    Result.Error(retrofitExceptionHandler.handleException(e))
                }
            }

            is Result.Error -> Result.Error(userId.error)
        }
    }
}
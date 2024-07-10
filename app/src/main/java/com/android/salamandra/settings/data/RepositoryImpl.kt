package com.android.salamandra.settings.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.settings.domain.Repository

class RepositoryImpl(
    private val cognitoService: CognitoService,
    private val localDbRepository: LocalDbRepository
) : Repository {
    override suspend fun logout(): Result<Unit, DataError> {
        localDbRepository.clearAllDatabase()
        return cognitoService.logout()
    }
}
package com.android.salamandra.authentication.verifyAccount.data

import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result

class RepositoryImpl(private val cognitoService: CognitoService): Repository {
    override suspend fun confirmRegister(
        username: String,
        code: String,
    ): Result<Unit, DataError.Cognito> = cognitoService.confirmRegister(username, code)
}
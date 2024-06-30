package com.android.salamandra.authentication.register.data

import com.android.salamandra.authentication.register.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result

class RepositoryImpl(private val cognitoService: CognitoService): Repository {
    override suspend fun register(
        email: String,
        password: String,
        username: String
    ): Result<Unit, DataError.Cognito> {
        return cognitoService.register(
            email,
            password,
            username
        )
    }


}
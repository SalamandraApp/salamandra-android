package com.android.salamandra.authentication.login.data

import com.android.salamandra.authentication.login.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result

class RepositoryImpl(private val cognitoService: CognitoService): Repository {

    override suspend fun login(email: String, password: String): Result<Unit, DataError.Cognito> {
        return when(val login = cognitoService.login(email, password)){
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(login.error)
        }
    }
}
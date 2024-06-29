package com.android.salamandra.authentication.verifyAccount.data

import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result

class RepositoryImpl(private val cognitoService: CognitoService): Repository {
    override suspend fun confirmRegister(
        username: String,
        code: String,
    ): Result<Unit, DataError.Cognito> {
        return when (val register = cognitoService.confirmRegister(username, code)){
            is Result.Success -> {
                //todo make call to backend to create user
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(register.error)
        }
    }
}
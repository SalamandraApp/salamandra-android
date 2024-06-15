package com.android.salamandra.authentication.register.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result


interface Repository {
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Result<Unit, DataError.Cognito>
}
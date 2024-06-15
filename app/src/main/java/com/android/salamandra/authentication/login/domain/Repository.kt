package com.android.salamandra.authentication.login.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result


interface Repository {
    suspend fun login(email: String, password: String): Result<Unit, DataError.Cognito>
}
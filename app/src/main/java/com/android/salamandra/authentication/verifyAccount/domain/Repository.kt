package com.android.salamandra.authentication.verifyAccount.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result


interface Repository {
    suspend fun confirmRegister(
        username: String,
        code: String,
        email: String,
        password: String
    ): Result<Unit, DataError>
}
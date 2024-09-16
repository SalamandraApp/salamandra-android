package com.android.salamandra._core.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.User
import kotlinx.coroutines.flow.Flow


interface CoreRepository {
    suspend fun isUserLogged(): Boolean
    suspend fun getUserData(): Result<User, DataError>
    suspend fun getUserDataAsFlow(): Flow<User?>?
}
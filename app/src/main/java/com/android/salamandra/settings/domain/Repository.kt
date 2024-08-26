package com.android.salamandra.settings.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result

interface Repository {
    suspend fun logout(): Result<Unit, DataError>
}
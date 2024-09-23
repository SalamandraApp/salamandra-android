package com.android.salamandra.settings.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import java.time.LocalDate

interface Repository {
    suspend fun logout(): Result<Unit, DataError>
    suspend fun changeDisplayName(newName: String?): Result<Unit, DataError>
    suspend fun changeDateOfBirth(newDateOfBirth: LocalDate?): Result<Unit, DataError>
}
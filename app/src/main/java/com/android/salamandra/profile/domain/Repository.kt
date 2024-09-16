package com.android.salamandra.profile.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel

interface Repository {
    suspend fun changeWeight(newWeight: Double?): Result<Unit, DataError>
    suspend fun changeFitnessLevel(newFitnessLevel: FitnessLevel?): Result<Unit, DataError>
    suspend fun changeFitnessGoal(newFitnessGoal: FitnessGoal?): Result<Unit, DataError>
}
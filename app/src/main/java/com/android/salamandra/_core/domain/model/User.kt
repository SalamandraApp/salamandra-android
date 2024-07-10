package com.android.salamandra._core.domain.model

import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.Gender
import java.time.LocalDate

data class User(
    val uid: String,
    val username: String,
    val displayName: String?,
    val dateJoined: LocalDate,
    val dateOfBirth: LocalDate?,
    val height: Int?,
    val weight: Double?,
    val gender: Gender?,
    val fitnessGoal: FitnessGoal?,
    val fitnessLevel: FitnessLevel?
)
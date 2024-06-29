package com.android.salamandra._core.domain.model

import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.Gender
import java.util.Date
import java.util.UUID

data class User(
    val uid: String,
    val username: String,
    val email: String,
    val displayName: String?,
    val dateJoined: Date,
    val dateOfBirth: Date?,
    val height: Int?,
    val weight: Double?,
    val gender: Gender?,
    val fitnessGoal: FitnessGoal?,
    val fitnessLevel: FitnessLevel?
)
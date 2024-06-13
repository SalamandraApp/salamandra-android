package com.android.salamandra.domain.model

import com.android.salamandra.domain.model.enums.FitnessGoal
import com.android.salamandra.domain.model.enums.FitnessLevel
import com.android.salamandra.domain.model.enums.Gender
import java.util.Date
import java.util.UUID

data class User(
    val uid: UUID,
    val username: String,
    val email: String,
    val displayName: String,
    val dateJoined: Date,
    val dateOfBirth: Date,
    val height: Int,
    val weight: Double,
    val gender: Gender,
    val fitnessGoal: FitnessGoal,
    val fitnessLevel: FitnessLevel
)
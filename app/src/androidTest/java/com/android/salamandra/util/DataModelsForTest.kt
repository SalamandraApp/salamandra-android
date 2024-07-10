package com.android.salamandra.util

import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.Gender
import java.time.LocalDate

val EXAMPLE_USER = User(
    uid = "123",
    username = "vzkz",
    displayName = "Vzkz",
    dateJoined = LocalDate.parse("2022-09-12"),
    dateOfBirth = LocalDate.parse("2004-03-11"),
    height = 183,
    weight = 76.5,
    gender = Gender.Male,
    fitnessGoal = FitnessGoal.Bulking,
    fitnessLevel = FitnessLevel.Beginner
    )

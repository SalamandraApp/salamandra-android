package com.android.salamandra.util

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.Gender
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
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
    fitnessLevel = FitnessLevel.Amateur
)

val EXAMPLE_EXERCISE_PUSH_UP = Exercise(
    exId = "exex1",
    name = "Push Up",
    mainMuscleGroup = MuscleGroup.Chest,
    secondaryMuscleGroup = MuscleGroup.Chest,
    necessaryEquipment = Equipment.Dumbbell,
    exerciseType = ExerciseType.Calisthenics
)

val EXAMPLE_EXERCISE_SQUAT = Exercise(
    exId = "exex2",
    name = "Squat",
    mainMuscleGroup = MuscleGroup.Quadriceps,
    secondaryMuscleGroup = MuscleGroup.Hamstrings,
    necessaryEquipment = Equipment.Barbell,
    exerciseType = ExerciseType.FreeWeights
)

val EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP = WkTemplateElement(
    templateElementId = "12345",
    exercise = EXAMPLE_EXERCISE_PUSH_UP,
    position = 1,
    reps = 15,
    sets = 4,
    weight = null,
    rest = 120
)

val EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT = WkTemplateElement(
    templateElementId = "12",
    exercise = EXAMPLE_EXERCISE_SQUAT,
    position = 1,
    reps = 15,
    sets = 4,
    weight = null,
    rest = 120
)

val EXAMPLE_WORKOUT_TEMPLATE = WorkoutTemplate(
    wkId = "12345",
    name = "Leg Hypertrophy",
    elements = listOf(
        EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP,
        EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT
    ),
    description = "This is a test workout to see if local db works",
    dateCreated = LocalDate.parse("2024-02-02")
)


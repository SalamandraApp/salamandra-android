package com.android.salamandra._core.util

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import java.util.UUID

val WORKOUT_PREVIEW = WorkoutPreview(wkId = UUID.randomUUID(), name = "Upper Body Hypertrophy")
val WORKOUT_PREVIEW2 = WorkoutPreview(wkId = UUID.randomUUID(), name = "Legs")

val WORKOUT_PREVIEW_LIST =
    listOf(WORKOUT_PREVIEW, WORKOUT_PREVIEW2, WORKOUT_PREVIEW, WORKOUT_PREVIEW2)

val EXERCISE = Exercise(
    UUID.randomUUID(),
    name = "Incline Bench press",
    mainMuscleGroup = MuscleGroup.Chest,
    secondaryMuscleGroup = MuscleGroup.Chest,
    necessaryEquipment = Equipment.Barbell,
    exerciseType = ExerciseType.Strength
)

val WORKOUT_TEMPLATE_ELEMENT = WkTemplateElement(
    exercise = EXERCISE,
    position = 1,
    reps = 3,
    sets = 4,
    weight = 80.0,
    rest = 120
)

val WORKOUT_TEMPLATE = WorkoutTemplate(elements = listOf(WORKOUT_TEMPLATE_ELEMENT, WORKOUT_TEMPLATE_ELEMENT.copy(weight = 78.56, exercise = EXERCISE.copy(name = "Legs"))))

val EXERCISE_LIST = listOf(EXERCISE, EXERCISE.copy(name = "Push ups"), EXERCISE.copy(name = "Dumbbell Bench press"), EXERCISE.copy(name = "Cable fly"))
val LONG_EXERCISE_LIST = List(4) { EXERCISE_LIST}.flatten()
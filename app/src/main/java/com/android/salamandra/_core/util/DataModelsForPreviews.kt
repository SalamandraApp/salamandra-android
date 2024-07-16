package com.android.salamandra._core.util

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import java.util.UUID

val WORKOUT_PREVIEW = WorkoutPreview(wkId = "", name = "Upper Body Hypertrophy Summer Training 2024")
val WORKOUT_PREVIEW2 = WorkoutPreview(wkId = "", name = "Legs")

val WORKOUT_PREVIEW_LIST =
    listOf(WORKOUT_PREVIEW, WORKOUT_PREVIEW2, WORKOUT_PREVIEW, WORKOUT_PREVIEW2)

val EXERCISE = Exercise(
    "",
    name = "Incline Bench Press",
    mainMuscleGroup = MuscleGroup.Chest,
    secondaryMuscleGroup = MuscleGroup.Chest,
    necessaryEquipment = Equipment.Barbell,
    exerciseType = ExerciseType.Strength
)

val WORKOUT_TEMPLATE_ELEMENT = WkTemplateElement(
    templateElementId = "",
    exercise = EXERCISE,
    position = 1,
)

val WORKOUT_TEMPLATE = WorkoutTemplate(wkId = "", elements = List(20) { WORKOUT_TEMPLATE_ELEMENT })


val EXERCISE_LIST = listOf(EXERCISE, EXERCISE.copy(name = "Push ups"), EXERCISE.copy(name = "Dumbbell Bench press"), EXERCISE.copy(name = "Cable fly"))
//val LONG_EXERCISE_LIST = List(4) { EXERCISE_LIST}.flatten()
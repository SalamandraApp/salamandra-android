package com.android.salamandra._core.util

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionElement
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionExercise
import com.android.salamandra._core.domain.model.workout.template.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.template.WorkoutPreview
import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate

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
    exerciseType = ExerciseType.FreeWeights
)

val WORKOUT_TEMPLATE_ELEMENT = WkTemplateElement(
    templateElementId = "",
    exercise = EXERCISE,
    position = 1,
)

val WORKOUT_TEMPLATE = WorkoutTemplate(wkId = "", elements = List(20) { WORKOUT_TEMPLATE_ELEMENT })

val WORKOUT_EXECUTION_ELEMENT = WkExecutionElement(
    currentSet = 1,
    reps = 12,
    weight = 75.0,
)


val WK_EXECUTION_EXERCISE = WkExecutionExercise(
    exercise = EXERCISE,
    executionElements = listOf(
        WORKOUT_EXECUTION_ELEMENT,
        WORKOUT_EXECUTION_ELEMENT.copy(currentSet = 2),
        WORKOUT_EXECUTION_ELEMENT.copy(currentSet = 3, weight = null),
        WORKOUT_EXECUTION_ELEMENT.copy(currentSet = 4),
    ),
    setNumber = 1,

)
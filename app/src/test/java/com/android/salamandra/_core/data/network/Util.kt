package com.android.salamandra._core.data.network

import com.android.salamandra._core.data.network.response.ExerciseResponse
import com.android.salamandra._core.data.network.response.SingleExercise
import com.android.salamandra._core.data.network.response.WkPreviewsResponse
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup

// searchExercise
val jsonSuccessfulSearchExerciseResponse = """
    {
      "exercises": [
        {
          "id": "4495fa60-f5c6-4ec5-9942-203acf751822",
          "name": "Push Up",
          "main_muscle_group": 1,
          "secondary_muscle_group": 15,
          "necessary_equipment": 1,
          "exercise_type": 1
        }
      ]
    }
""".trimIndent()

val parsedExerciseResponse = ExerciseResponse(
    items = listOf(SingleExercise(
        id = "4495fa60-f5c6-4ec5-9942-203acf751822",
        name = "Push Up",
        mainMuscleGroup = 1,
        secondaryMuscleGroup = 15,
        necessaryEquipment = 1,
        exerciseType = 1
    ))
)

val toDomainExerciseResponse = listOf(Exercise(
    exId = "4495fa60-f5c6-4ec5-9942-203acf751822",
    name = "Push Up",
    mainMuscleGroup = MuscleGroup.LowerChest,
    secondaryMuscleGroup = MuscleGroup.Quadriceps,
    necessaryEquipment = Equipment.Barbell,
    exerciseType = ExerciseType.FreeWeights
))

// getWkPreviews
val jsonSuccessfulGetWkPreviewsResponse = """
    {
      "exercises": [
        {
          "id": "4495fa60-f5c6-4ec5-9942-203acf751822",
          "name": "Push Up",
          "main_muscle_group": 1,
          "secondary_muscle_group": 15,
          "necessary_equipment": 1,
          "exercise_type": 1
        }
      ]
    }
""".trimIndent()

val parsedGetWkPreviewsResponse = WkPreviewsResponse(
    count = 0,
    items = emptyList()
)

val toDomainGetWkPreviewsResponse = listOf(Exercise(
    exId = "4495fa60-f5c6-4ec5-9942-203acf751822",
    name = "Push Up",
    mainMuscleGroup = MuscleGroup.LowerChest,
    secondaryMuscleGroup = MuscleGroup.Quadriceps,
    necessaryEquipment = Equipment.Barbell,
    exerciseType = ExerciseType.FreeWeights
))


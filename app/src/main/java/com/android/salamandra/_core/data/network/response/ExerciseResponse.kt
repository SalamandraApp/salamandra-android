package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ExerciseResponse(
    @SerializedName("exercises") val items: List<SingleExercise>?
) {
    fun toDomain(): List<Exercise>? {
        return items?.map { it.toDomain() }
    }
}

data class SingleExercise(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
) {
    fun toDomain(): Exercise {
        return Exercise(
            exId = id,
            name = name,
            mainMuscleGroup = MuscleGroup.Chest,
            secondaryMuscleGroup = MuscleGroup.Chest,
            necessaryEquipment = Equipment.Barbell,
            exerciseType = ExerciseType.FreeWeights
        )
    }
}
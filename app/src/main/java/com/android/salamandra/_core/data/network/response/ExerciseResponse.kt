package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.enums.toEquipment
import com.android.salamandra._core.domain.model.enums.toExerciseType
import com.android.salamandra._core.domain.model.enums.toMuscleGroup
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
    @SerializedName("name") val name: String,
    @SerializedName("main_muscle_group") val mainMuscleGroup: Int,
    @SerializedName("secondary_muscle_group") val secondaryMuscleGroup: Int,
    @SerializedName("necessary_equipment") val necessaryEquipment: Int,
    @SerializedName("exercise_type") val exerciseType: Int,
) {
    fun toDomain(): Exercise {
        return Exercise(
            exId = id,
            name = name,
            mainMuscleGroup = mainMuscleGroup.toMuscleGroup(),
            secondaryMuscleGroup = secondaryMuscleGroup.toMuscleGroup(),
            necessaryEquipment = necessaryEquipment.toEquipment(),
            exerciseType = exerciseType.toExerciseType()
        )
    }
}
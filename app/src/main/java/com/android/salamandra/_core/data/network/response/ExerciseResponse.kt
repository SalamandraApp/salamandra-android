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
    @SerializedName("main_muscle_group") val mainMuscleGroup: Short,
    @SerializedName("secondary_muscle_group") val secondaryMuscleGroup: Short,
    @SerializedName("necessary_equipment") val necessaryEquipment: Short,
    @SerializedName("exercise_type") val exerciseType: Short,
) {
    fun toDomain(): Exercise {
        return Exercise(
            exId = id,
            name = name,
            mainMuscleGroup = mainMuscleGroup.toInt().toMuscleGroup(),
            secondaryMuscleGroup = secondaryMuscleGroup.toInt().toMuscleGroup(),
            necessaryEquipment = necessaryEquipment.toInt().toEquipment(),
            exerciseType = exerciseType.toInt().toExerciseType()
        )
    }
}
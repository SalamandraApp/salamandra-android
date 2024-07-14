package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.enums.toEquipment
import com.android.salamandra._core.domain.model.enums.toExerciseType
import com.android.salamandra._core.domain.model.enums.toMuscleGroup
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class WkTemplateResponse(
    @SerializedName("id") private val id: String,
    @SerializedName("user_id") private val userId: String,
    @SerializedName("name") private val name: String,
    @SerializedName("description") private val description: String?,
    @SerializedName("date_created") private val dateCreated: LocalDate,
    @SerializedName("elements") private val elements: List<ExerciseInWkTemplateResponse>,
) {
    fun toDomain(): WorkoutTemplate {
        return WorkoutTemplate(
            wkId = id,
            name = name,
            elements = elements.map { it.toDomain() },
            description = description,
            dateCreated = dateCreated,
        )
    }
}

data class ExerciseInWkTemplateResponse(
    @SerializedName("id") private val templateElementId: String,
    @SerializedName("exercise_id") private val exerciseId: String,
    @SerializedName("exercise_name") private val exName: String,
    @SerializedName("mainMuscleGroup") private val mainMuscleGroup: Int,
    @SerializedName("secondaryMuscleGroup") private val secondaryMuscleGroup: Int,
    @SerializedName("necessaryEquipment") private val necessaryEquipment: Int,
    @SerializedName("exerciseType") private val exerciseType: Int,
    @SerializedName("position") private val position: Int,
    @SerializedName("reps") private val reps: Int,
    @SerializedName("sets") private val sets: Int,
    @SerializedName("weight") private val weight: Double,
    @SerializedName("rest") private val rest: Int,
    @SerializedName("super_set") private val superSet: Boolean,
) {
    fun toDomain(): WkTemplateElement {
        return WkTemplateElement(
            templateElementId = templateElementId,
            exercise = Exercise(
                exId = exerciseId,
                name = exName,
                mainMuscleGroup = mainMuscleGroup.toMuscleGroup(),
                secondaryMuscleGroup = secondaryMuscleGroup.toMuscleGroup(),
                necessaryEquipment = necessaryEquipment.toEquipment(),
                exerciseType = exerciseType.toExerciseType()
            ),
            position = position,
            reps = reps,
            sets = sets,
            weight = weight,
            rest = rest
        )
    }
}





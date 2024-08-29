package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.toEquipment
import com.android.salamandra._core.domain.model.enums.toExerciseType
import com.android.salamandra._core.domain.model.enums.toMuscleGroup
import com.android.salamandra._core.domain.model.workout.template.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class WorkoutTemplateResponse(
    @SerializedName("id") private val id: String,
    @SerializedName("user_id") private val userId: String,
    @SerializedName("name") private val name: String,
    @SerializedName("description") private val description: String?,
    @SerializedName("date_created") private val dateCreated: String,
    @SerializedName("elements") private val elements: List<ExerciseInWkTemplateResponse>,
) {
    fun toDomain(): WorkoutTemplate {
        return WorkoutTemplate(
            wkId = id,
            name = name,
            elements = elements.map { it.toDomain() },
            description = description,
            dateCreated = LocalDate.parse(dateCreated),
        )
    }
}

data class ExerciseInWkTemplateResponse(
    @SerializedName("id") private val templateElementId: String,
    @SerializedName("exercise_id") private val exerciseId: String,
    @SerializedName("exercise_name") private val exName: String,
    @SerializedName("main_muscle_group") private val mainMuscleGroup: Short,
    @SerializedName("secondary_muscle_group") private val secondaryMuscleGroup: Short,
    @SerializedName("necessary_equipment") private val necessaryEquipment: Short,
    @SerializedName("exercise_type") private val exerciseType: Short,
    @SerializedName("position") private val position: Short,
    @SerializedName("reps") private val reps: Short,
    @SerializedName("sets") private val sets: Short,
    @SerializedName("weight") private val weight: Double?,
    @SerializedName("rest") private val rest: Short,
    @SerializedName("super_set") private val superset: Short?,
) {
    fun toDomain(): WkTemplateElement {
        return WkTemplateElement(
            templateElementId = templateElementId,
            exercise = Exercise(
                exId = exerciseId,
                name = exName,
                mainMuscleGroup = mainMuscleGroup.toInt().toMuscleGroup(),
                secondaryMuscleGroup = secondaryMuscleGroup.toInt().toMuscleGroup(),
                necessaryEquipment = necessaryEquipment.toInt().toEquipment(),
                exerciseType = exerciseType.toInt().toExerciseType()
            ),
            position = position.toInt(),
            reps = reps.toInt(),
            sets = sets.toInt(),
            weight = weight,
            rest = rest.toInt(),
            superset = superset?.toInt()
        )
    }
}





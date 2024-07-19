package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.toEquipment
import com.android.salamandra._core.domain.model.enums.toExerciseType
import com.android.salamandra._core.domain.model.enums.toMuscleGroup
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class CreateWorkoutTemplateResponse(
    @SerializedName("id") private val id: String,
    @SerializedName("user_id") private val userId: String,
    @SerializedName("name") private val name: String,
    @SerializedName("description") private val description: String?,
    @SerializedName("date_created") private val dateCreated: String,
    @SerializedName("elements") private val elements: List<ExerciseInCreateWkTemplateResponse>,
) {
    fun toDomain(workoutTemplate: WorkoutTemplate): WorkoutTemplate {
        var newWkTemplate = workoutTemplate.copy(wkId = id)
        newWkTemplate =
            newWkTemplate.copy(elements = newWkTemplate.elements.mapIndexed() { index, element ->
                if (element.exercise.exId == elements[index].exerciseId)
                    element.copy(templateElementId = elements[index].templateElementId)
                else throw Exception("Error parsing create workout response")
            })
        return newWkTemplate
    }
}

data class ExerciseInCreateWkTemplateResponse(
    @SerializedName("id") val templateElementId: String,
    @SerializedName("exercise_id") val exerciseId: String,
    @SerializedName("position") val position: Int,
    @SerializedName("reps") val reps: Int,
    @SerializedName("sets") val sets: Int,
    @SerializedName("weight") val weight: Double,
    @SerializedName("rest") val rest: Int,
    @SerializedName("super_set") val superSet: Boolean,
)





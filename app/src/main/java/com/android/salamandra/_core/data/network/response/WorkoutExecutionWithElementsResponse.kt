package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.data.network.request.CreateWorkoutExecutionElementRequest
import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate
import com.google.gson.annotations.SerializedName

data class WorkoutExecutionWithElementsResponse(
    @SerializedName("id") val workoutExecutionId: String,
    @SerializedName("workout_template_id") val workoutTemplateId: String,
    @SerializedName("survey") val survey: Short?,
    @SerializedName("date") val date: String,
    @SerializedName("elements") val elements: List<CreateWorkoutExecutionElementRequest>
) {
//    fun toDomain(workoutTemplate: WorkoutTemplate): WorkoutTemplate {
//        var newWkTemplate = workoutTemplate.copy(wkId = id)
//        newWkTemplate =
//            newWkTemplate.copy(elements = newWkTemplate.elements.mapIndexed() { index, element ->
//                if (element.exercise.exId == elements[index].exerciseId)
//                    element.copy(templateElementId = elements[index].templateElementId)
//                else throw Exception("Error parsing create workout response")
//            })
//        return newWkTemplate
//    }
}

data class ElementInWkExecutionWithElementsResponse(
    @SerializedName("id") val workoutExecutionElementId: String,
    @SerializedName("exercise_id") val exerciseId: String,
    @SerializedName("position") val position: Short,
    @SerializedName("exercise_number") val exerciseNumber: Short,
    @SerializedName("reps") val reps: Short,
    @SerializedName("set_number") val setNumber: Short,
    @SerializedName("weight") val weight: Double?,
    @SerializedName("rest") val rest: Short,
    @SerializedName("super_set") val superSet: Short?,
    @SerializedName("time") val time: Int
)
package com.android.salamandra._core.data.network.request

import com.android.salamandra._core.domain.model.workout.template.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.template.WorkoutTemplate
import com.google.gson.annotations.SerializedName

data class CreateWorkoutTemplateRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("date_created") val dateCreated: String,
    @SerializedName("elements") val elements: List<CreateWorkoutTemplateElementRequest>
)

data class CreateWorkoutTemplateElementRequest(
    @SerializedName("exercise_id") val exerciseId: String,
    @SerializedName("position") val position: Short,
    @SerializedName("reps") val reps: Short,
    @SerializedName("sets") val sets: Short,
    @SerializedName("weight") val weight: Double?,
    @SerializedName("rest") val rest: Short,
    @SerializedName("super_set") val superSet: Short?
)

fun WorkoutTemplate.toCreateWorkoutTemplateRequest() =
    CreateWorkoutTemplateRequest(
        name = name,
        description = description,
        dateCreated = dateCreated?.toString() ?: throw IllegalArgumentException("DateCreated is mandatory for creating a workout"),
        elements = elements.map { it.toCreateWorkoutTemplateElementRequest() }
    )

fun WkTemplateElement.toCreateWorkoutTemplateElementRequest() = CreateWorkoutTemplateElementRequest(
    exerciseId = exercise.exId,
    position = position?.toShort() ?: throw IllegalArgumentException("A non null position is mandatory"),
    reps = reps.toShort(),
    sets = sets.toShort(),
    weight = weight,
    rest = rest.toShort(),
    superSet = superset?.toShort(),
)
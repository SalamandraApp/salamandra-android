package com.android.salamandra._core.data.network.request

import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class CreateWorkoutTemplateRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("date_created") val dateCreated: String,
    @SerializedName("elements") val elements: List<CreateWorkoutTemplateElementRequest>
)

data class CreateWorkoutTemplateElementRequest(
    @SerializedName("exercise_id") val exerciseId: String,
    @SerializedName("position") val position: Int,
    @SerializedName("reps") val reps: Int,
    @SerializedName("sets") val sets: Int,
    @SerializedName("weight") val weight: Double?,
    @SerializedName("rest") val rest: Int,
    @SerializedName("super_set") val superSet: Int?
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
    position = position,
    reps = reps,
    sets = sets,
    weight = weight,
    rest = rest,
    superSet = superSet,
)
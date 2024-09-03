package com.android.salamandra._core.data.network.request

import com.android.salamandra._core.domain.model.workout.executions.WkExecutionElement
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionExercise
import com.android.salamandra._core.domain.model.workout.executions.WorkoutExecution
import com.google.gson.annotations.SerializedName

data class CreateWorkoutExecutionRequest(
    @SerializedName("workout_template_id") val workoutTemplateId: String,
    @SerializedName("survey") val survey: Short?,
    @SerializedName("date") val date: String,
    @SerializedName("elements") val elements: List<CreateWorkoutExecutionElementRequest>
)

data class CreateWorkoutExecutionElementRequest(
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

fun WorkoutExecution.toCreateWorkoutExecutionRequest(workoutTemplateId: String): CreateWorkoutExecutionRequest {
    val elementsRequest: MutableList<CreateWorkoutExecutionElementRequest> = mutableListOf()
    var position: Short = 0
    elements.map { wkExecutionExercise ->
        elementsRequest.addAll(wkExecutionExercise.executionElements.map { wkExecutionElement ->
            position = (position + 1).toShort()
            wkExecutionElement.toCreateWorkoutExecutionElementRequest(
                exerciseId = wkExecutionExercise.exercise.exId,
                exerciseNumber = wkExecutionExercise.exerciseNumber.toShort(),
                position = position
            )
        })
    }

    return CreateWorkoutExecutionRequest(
        workoutTemplateId = workoutTemplateId,
        survey = survey?.toShort() ?: 1,
        date = date.toString(),
        elements = elementsRequest
    )
}

fun WkExecutionElement.toCreateWorkoutExecutionElementRequest(
    exerciseId: String,
    exerciseNumber: Short,
    position: Short
): CreateWorkoutExecutionElementRequest {
    return CreateWorkoutExecutionElementRequest(
        exerciseId = exerciseId,
        position = position,
        exerciseNumber = exerciseNumber,
        reps = reps.toShort(),
        setNumber = setNumber.toShort(),
        weight = weight,
        rest = rest.toShort(),
        superSet = superset?.toShort(),
        time = time
    )
}

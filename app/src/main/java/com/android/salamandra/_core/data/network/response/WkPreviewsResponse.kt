package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class WkPreviewsResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("templates") val items: List<WkTemplate>,
) {
    fun toDomain(): List<WorkoutPreview> {
        return items.map { it.toDomain() }
    }
}

data class WkTemplate(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
) {
    fun toDomain():  WorkoutPreview {
        return WorkoutPreview (
            wkId = id,
            name = name,
        )
    }
}
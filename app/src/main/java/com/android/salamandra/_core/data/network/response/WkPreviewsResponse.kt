package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.workout.template.WorkoutPreview
import com.google.gson.annotations.SerializedName

data class WkPreviewsResponse(
    @SerializedName("count") val count: Short,
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
    fun toDomain(): WorkoutPreview {
        return WorkoutPreview (
            wkId = id,
            name = name,
        )
    }
}
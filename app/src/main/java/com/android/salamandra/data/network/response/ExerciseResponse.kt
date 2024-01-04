package com.android.salamandra.data.network.response

import com.android.salamandra.domain.model.ExerciseModel
import com.google.gson.annotations.SerializedName

data class ExerciseResponse(
    @SerializedName("items") val items: List<SingleExercise>?
){
    fun toDomain(): List<ExerciseModel>?{
        return items?.map { it.toDomain() }
    }
}

data class SingleExercise(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
){
    fun toDomain(): ExerciseModel{
        return ExerciseModel(id = this.id, name = this.name)
    }
}
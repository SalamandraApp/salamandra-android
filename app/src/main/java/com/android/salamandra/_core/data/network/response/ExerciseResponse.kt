package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.Exercise
import com.google.gson.annotations.SerializedName

data class ExerciseResponse(
    @SerializedName("items") val items: List<SingleExercise>?
){
    fun toDomain(): List<Exercise>?{
//        return items?.map { it.toDomain() }
        TODO()
    }
}

data class SingleExercise(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
){

}
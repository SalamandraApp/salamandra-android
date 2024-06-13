package com.android.salamandra.data.network.response

import com.android.salamandra.domain.model.Exercise
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
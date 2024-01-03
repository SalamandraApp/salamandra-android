package com.android.salamandra.data.network.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("error") val error: String,
    @SerializedName("token") val token: String,
) {
//    fun toDomain(): ExerciseModel = ExerciseModel(exName = name, muscle = muscle, difficulty = difficulty, instructions = instructions)
}
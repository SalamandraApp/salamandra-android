package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.toFitnessGoal
import com.android.salamandra._core.domain.model.enums.toFitnessLevel
import com.android.salamandra._core.domain.model.enums.toGender
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale


data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("date_joined") val dateJoined: String,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    @SerializedName("height") val height: Short?,
    @SerializedName("weight") val weight: Float?,
    @SerializedName("gender") val gender: Short?,
    @SerializedName("fitness_goal") val fitnessGoal: Short?,
    @SerializedName("fitness_level") val fitnessLevel: Short?
) {
    fun toDomain(): User {
        return User(
            uid = id,
            username = username,
            displayName = displayName,
            dateJoined = LocalDate.parse(dateJoined),
            dateOfBirth = if (dateOfBirth != null) LocalDate.parse(dateOfBirth) else null,
            height = height?.toInt(),
            weight = weight?.toDouble(),
            gender = gender?.toInt()?.toGender(),
            fitnessGoal = fitnessGoal?.toInt()?.toFitnessGoal(),
            fitnessLevel = fitnessLevel?.toInt()?.toFitnessLevel()
        )
    }
}


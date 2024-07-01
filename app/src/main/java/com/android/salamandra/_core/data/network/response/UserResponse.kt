package com.android.salamandra._core.data.network.response

import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.toFitnessGoal
import com.android.salamandra._core.domain.model.enums.toFitnessLevel
import com.android.salamandra._core.domain.model.enums.toGender
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale


data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("date_joined") val dateJoined: String,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    @SerializedName("height") val height: Int?,
    @SerializedName("weight") val weight: Float?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("fitness_goal") val fitnessGoal: Int?,
    @SerializedName("fitness_level") val fitnessLevel: Int?
) {
    fun toDomain(): User {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return User(
            uid = id,
            username = username,
            displayName = displayName,
            dateJoined = dateFormat.parse(dateJoined)
                ?: throw IllegalArgumentException("Invalid date format"),
            dateOfBirth = if (dateOfBirth != null) dateFormat.parse(dateOfBirth)
                ?: throw IllegalArgumentException("Invalid date format") else null,
            height = height,
            weight = weight?.toDouble(),
            gender = gender?.toGender(),
            fitnessGoal = fitnessGoal?.toFitnessGoal(),
            fitnessLevel = fitnessLevel?.toFitnessLevel()
        )
    }
}


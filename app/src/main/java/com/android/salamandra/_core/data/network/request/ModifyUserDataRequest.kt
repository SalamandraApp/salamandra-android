package com.android.salamandra._core.data.network.request

import com.google.gson.annotations.SerializedName

data class ModifyUserDataRequest(
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("date_joined") val dateJoined: String? = null,
    @SerializedName("date_of_birth") val dateOfBirth: String? = null,
    @SerializedName("height") val height: Short? = null,
    @SerializedName("weight") val weight: Float? = null,
    @SerializedName("gender") val gender: Short? = null,
    @SerializedName("fitness_goal") val fitnessGoal: Short? = null,
    @SerializedName("fitness_level") val fitnessLevel: Short? = null
)

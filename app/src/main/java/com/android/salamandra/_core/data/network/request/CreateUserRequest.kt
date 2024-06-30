package com.android.salamandra._core.data.network.request

import com.google.gson.annotations.SerializedName

data class CreateUserRequest(
    @SerializedName("display_name") val id: String,
    @SerializedName("display_name") val username: String,
    @SerializedName("date_joined") val dateJoined: String,
)
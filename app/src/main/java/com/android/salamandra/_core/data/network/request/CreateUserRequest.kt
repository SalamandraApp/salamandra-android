package com.android.salamandra._core.data.network.request

import com.google.gson.annotations.SerializedName

data class CreateUserRequest(
    @SerializedName("uuid") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("date_joined") val dateJoined: String,
)
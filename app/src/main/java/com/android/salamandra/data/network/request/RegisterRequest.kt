package com.android.salamandra.data.network.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
package com.android.salamandra._core.data.network.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("error") val error: String,
    @SerializedName("token") val token: String,
)
package com.android.salamandra._core.data.network.request

import com.google.gson.annotations.SerializedName

data class AccessTokenRequest(
    @SerializedName("grant_type") val grantType: String,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_secret") val clientSecret: String
)
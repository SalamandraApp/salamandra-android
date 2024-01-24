package com.android.salamandra.data.network.response

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_expires_in") val refreshExpiresIn: Int,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("not-before-policy") val notBeforePolicy: Int,
    @SerializedName("scope") val scope: String
)

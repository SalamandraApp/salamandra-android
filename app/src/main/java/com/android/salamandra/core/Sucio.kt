package com.android.salamandra.core

import com.android.salamandra.data.network.request.AuthRequest
import com.android.salamandra.data.network.response.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//Different ways to do API calls
/*
        val response = salamandraApiService.getAccessToken(ACCESSTOKENREQUEST.grantType,ACCESSTOKENREQUEST.clientId,ACCESSTOKENREQUEST.clientSecret).enqueue(
            object : Callback<AccessTokenResponse> {
                override fun onResponse(
                    call: Call<AccessTokenResponse>,
                    response: Response<AccessTokenResponse>
                ) {
                    Log.i("Jaime", "dummy")
                }

                override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                    Log.i("Jaime", "dummy")
                }

            }
        )
*/

/*
salamandraApiService.login(AuthRequest(username = email, password = password)).enqueue(
    object : Callback<AuthResponse> {
        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
            onResponse(Pair(false, "Failure during API call to login"))
        }
        override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
            checkResponse(response, onResponse)
        }
    }
)
*/

/*
override suspend fun register(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit) {
    salamandraApiService.register(AuthRequest(username = email, password = password)).enqueue(
        object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onResponse(Pair(false, "Failure during API call to register"))
            }
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                checkResponse(response, onResponse)
            }
        }
    )
}

private fun checkResponse(
    response: Response<AuthResponse>,
    onResponse: (Pair<Boolean, String>) -> Unit
) {
    if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null)
            onResponse(Pair(true, responseBody.token))
        else
            onResponse(Pair(false, "Null response body")) //Code should never get here
    } else {
        val errorMsg = response.errorBody()?.string() ?: "Unknown error"
        onResponse(Pair(false, errorMsg))
    }
}*/

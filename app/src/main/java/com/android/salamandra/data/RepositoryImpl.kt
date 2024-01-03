package com.android.salamandra.data

import com.android.salamandra.data.network.AuthApiService
import com.android.salamandra.data.network.request.LoginRequest
import com.android.salamandra.data.network.request.RegisterRequest
import com.android.salamandra.data.network.response.LoginResponse
import com.android.salamandra.data.network.response.RegisterResponse
import com.android.salamandra.domain.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
) : Repository {
    override suspend fun login(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit){ //Pair: Exit/Failure + msg
        authApiService.login(LoginRequest(username = email, password = password)).enqueue(
            object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResponse(Pair(false, "Failure during API call to login"))
                }
                override fun onResponse( call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val responseBody = response.body()
                    if(responseBody != null){
                        if(responseBody.error != "None"){
                            onResponse(Pair(false, responseBody.error))
                        }else{
                            onResponse(Pair(true, responseBody.token))
                        }
                    }
                    else {
                        onResponse(Pair(false, "null login response"))
                    }
                }
            }
        )
    }

    override suspend fun register(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit) {
        authApiService.register(RegisterRequest(username = email, password = password)).enqueue(
            object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    onResponse(Pair(false, "Failure during API call to register"))
                }
                override fun onResponse( call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    val responseBody = response.body()
                    if(responseBody != null){
                        if(responseBody.error != "None"){
                            onResponse(Pair(false, responseBody.error))
                        }else{
                            onResponse(Pair(true, responseBody.token))
                        }
                    }
                    else {
                        onResponse(Pair(false, "null register response"))
                    }
                }
            }
        )
    }

}

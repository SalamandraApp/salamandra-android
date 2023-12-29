package com.android.salamandra.data

import android.util.Log
import com.android.salamandra.data.network.AuthApiService
import com.android.salamandra.data.network.request.LoginRequest
import com.android.salamandra.data.network.response.LoginResponse
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.UserModel
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Response

class RepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
) : Repository {
    override suspend fun login(email: String, password: String): UserModel {
        val response = authApiService.login(LoginRequest(username = email, password = password))

        response.enqueue(
            object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    //handle error
                }
                override fun onResponse( call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginInfo = response.body()
                    Log.i("Jaime", "$loginInfo")
                }
            }
        )


        return UserModel("tokenExample", "jaime")
    }

    override suspend fun register(email: String, password: String): UserModel {
        TODO("Not yet implemented")
    }

}

package com.android.salamandra.data

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.android.salamandra.core.ACCESSTOKENREQUEST
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
) : Repository {

    //Auth
    override suspend fun login(
        email: String,
        password: String,
        onResponse: (Pair<Boolean, String>) -> Unit
    ) { //Pair: Exit/Failure + msg
        TODO("Not yet implemented")

    }

    override suspend fun register(email: String, password: String, username: String) { //throws exception
        val options = AuthSignUpOptions.builder()
//            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        try {
            Amplify.Auth.signUp(
                email ,
                password,
                options,
                {//onSuccess
                    Log.i("Jaime", "Amplify register worked")
                },
                {//onError
                    throw(it)
                }
            )
        } catch (error: AuthException) {
            Log.e("Jaime", "Sign up failed", error)
            throw Exception(error.message)
        }
    }

    override suspend fun confirmRegister(username: String, code: String){
        try {
            Amplify.Auth.confirmSignUp(
                username,
                code,
                {//onSuccess
                    Log.i("Jaime", "Signup confirmed")
                },
                {//onError
                    Log.i("Jaime", "Signup confirmation not yet complete: ${it.message}")
                }
            )
        } catch (error: AuthException) {
            Log.e("AuthQuickstart", "Failed to confirm signup", error)
        }

    }

    suspend fun getAccessToken() {
        runCatching {
            salamandraApiService.getAccessToken(
                ACCESSTOKENREQUEST.grantType,
                ACCESSTOKENREQUEST.clientId,
                ACCESSTOKENREQUEST.clientSecret
            )
        }
            .onSuccess {
                val response = it
                Log.i("Jaime", "dummy log")

            }
            .onFailure {
                Log.i("Jaime", "An error ocurred while using apiService, ${it.message}")
            }
    }

    //Ex query
    override suspend fun getExercise(term: String): List<ExerciseModel>? {
        runCatching {
            salamandraApiService.searchExercise(term)
        }
            .onSuccess {
                return it.toDomain()
            }
            .onFailure {
                Log.i("Jaime", "An error ocurred while using apiService, ${it.message}")
            }
        return null
    }
}

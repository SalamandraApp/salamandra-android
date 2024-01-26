package com.android.salamandra.data

import android.util.Log
import com.android.salamandra.core.ACCESSTOKENREQUEST
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
) : Repository {

    //Auth
    override suspend fun login(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit){ //Pair: Exit/Failure + msg
        TODO("Not yet implemented")

    }

    override suspend fun register(
        email: String,
        password: String,
        onResponse: (Pair<Boolean, String>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    suspend fun getAccessToken(){
        runCatching {
            salamandraApiService.getAccessToken(ACCESSTOKENREQUEST.grantType,ACCESSTOKENREQUEST.clientId,ACCESSTOKENREQUEST.clientSecret)
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

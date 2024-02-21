package com.android.salamandra.data

import android.util.Log
import com.android.salamandra.data.cognito.CognitoService
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import com.android.salamandra.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
    private val cognitoService: CognitoService
) : Repository {

    //Auth
    override suspend fun login(email: String, password: String): Result<UserModel> {
        return if(cognitoService.login(email, password)){
            //todo fetch user data from db
            Result.success(UserModel())
        } else{
            Result.failure(Exception("Login failed"))
        }
    }

    override suspend fun register(email: String, password: String, username: String): Boolean {
        return try{
            cognitoService.register(email, password, username)
        } catch (e: Exception){
            Log.e("Jaime", "An error ocurred while using cognitoService, ${e.message}")
            throw e
        }
    }

    override suspend fun confirmRegister(username: String, code: String) =
        cognitoService.confirmRegister(username, code)

    override suspend fun logout() = cognitoService.logout()

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



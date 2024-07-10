package com.android.salamandra.authentication.login.data

import android.util.Log
import com.android.salamandra.authentication.login.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import retrofit2.HttpException

class RepositoryImpl(
    private val cognitoService: CognitoService,
    private val salamandraApiService: SalamandraApiService,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
): Repository {

    override suspend fun login(email: String, password: String): Result<Unit, DataError> {
        return when(val login = cognitoService.login(email, password)){
            is Result.Success -> {
                return try {
                    val userData = salamandraApiService.getUserData(login.data).toDomain()
                    Result.Success(Unit)
                } catch (httpException: HttpException){
                    Result.Error(retrofitExceptionHandler.handleHTTPException(httpException))

                } catch (e: Exception){
                    //Result.Error(retrofitExceptionHandler.handleNoConnectionException(connectException))
                    Log.e("SLM", e.message.toString())
                    Result.Success(Unit) //TODO change
                }
            }
            is Result.Error -> Result.Error(login.error)
        }
    }
}
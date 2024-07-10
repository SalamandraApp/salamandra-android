package com.android.salamandra.authentication.verifyAccount.data

import com.android.salamandra._core.data.adapter.DateAdapter
import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.network.request.CreateUserRequest
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import java.time.LocalDate

class RepositoryImpl(
    private val cognitoService: CognitoService,
    private val salamandraApiService: SalamandraApiService,
    private val dateAdapter: DateAdapter,
    private val retrofitExceptionHandler: RetrofitExceptionHandler
) : Repository {
    override suspend fun confirmRegister(
        username: String,
        code: String,
        email: String,
        password: String
    ): Result<Unit, DataError> {
        return when (val register = cognitoService.confirmRegister(
            username = username,
            code = code,
            email = email,
            password = password
        )) {
            is Result.Success -> {
                try {
                    salamandraApiService.createUser(
                        CreateUserRequest(
                            id = register.data,
                            username = username,
                            dateJoined = dateAdapter.encode(LocalDate.now())
                        )
                    )
                    Result.Success(Unit)
                } catch (exception: Exception) {
                    Result.Error(retrofitExceptionHandler.handleException(exception))
                }
            }

            is Result.Error -> Result.Error(register.error)
        }
    }
}
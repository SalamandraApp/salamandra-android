package com.android.salamandra.authentication.register.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra.authentication.register.domain.Repository

class RepositoryImpl(private val cognitoService: CognitoService) : Repository {
    override suspend fun register(
        email: String,
        password: String,
        username: String
    ) = cognitoService.register(
        email,
        password,
        username
    )
}
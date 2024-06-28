package com.android.salamandra._core.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.CoreRepository

class CoreRepositoryImpl(private val cognitoService: CognitoService): CoreRepository {

   override suspend fun isUserLogged() = cognitoService.isUserLogged()
}
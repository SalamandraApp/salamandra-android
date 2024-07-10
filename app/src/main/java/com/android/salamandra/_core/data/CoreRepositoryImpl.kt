package com.android.salamandra._core.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.sqlDelight.user.toUser
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.User

class CoreRepositoryImpl(
   private val cognitoService: CognitoService,
   private val dataStoreRepository: DataStoreRepository,
   private val localDbRepository: LocalDbRepository
): CoreRepository {

   override suspend fun isUserLogged() = cognitoService.isUserLogged()

   override suspend fun getUserData(): Result<User, DataError> {
      return when(val userId = dataStoreRepository.getUidFromDatastore()){
         is Result.Success -> {
            when(val user = localDbRepository.getUserByID(userId.data)){
               is Result.Success -> Result.Success(user.data.toUser())
               is Result.Error -> Result.Error(user.error)
            }
         }
         is Result.Error -> Result.Error(userId.error)
      }
   }
}
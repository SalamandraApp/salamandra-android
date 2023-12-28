package com.android.salamandra.data

import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {
    override suspend fun login(email: String, password: String): UserModel {
        TODO("Not yet implemented")
    }

}

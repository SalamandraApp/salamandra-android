package com.android.salamandra.domain.usecases

import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.UserModel
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(email: String, password: String): UserModel {
       return repository.login(email, password)
    }
}
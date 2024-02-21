package com.android.salamandra.domain.usecases.auth

import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.UserModel
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.logout()
}
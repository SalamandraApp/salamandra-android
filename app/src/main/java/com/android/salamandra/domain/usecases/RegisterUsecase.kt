package com.android.salamandra.domain.usecases

import com.android.salamandra.domain.Repository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: Repository) {
    suspend fun register(email: String, username: String, password: String) = repository.register(email = email, password = password, username = username)
    suspend fun confirmRegister(username: String, code: String) = repository.confirmRegister(username = username, code = code)
}
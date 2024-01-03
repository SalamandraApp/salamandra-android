package com.android.salamandra.domain.usecases

import com.android.salamandra.domain.Repository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit) {
        repository.register(email, password){
            onResponse(it)
        }
    }
}
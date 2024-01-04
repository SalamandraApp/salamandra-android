package com.android.salamandra.domain.usecases

import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import com.android.salamandra.domain.model.UserModel
import javax.inject.Inject

class SearchExerciseUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(term: String): List<ExerciseModel>? {
       return repository.getExercise(term)
    }
}
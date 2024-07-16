package com.android.salamandra.workouts.editWk.data

import android.util.Log
import com.android.salamandra._core.data.sqlDelight.exercise.toExercise
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra.workouts.editWk.domain.Repository

class RepositoryImpl(private val localDbRepository: LocalDbRepository): Repository {

    override suspend fun getAllExercises(exerciseIdList: Array<String>): List<Exercise>{
        val exerciseList = mutableListOf<Exercise>()
        exerciseIdList.forEach { id ->
            when(val exercise = localDbRepository.getExerciseByID(id)){
                is Result.Success -> exerciseList.add(exercise.data.toExercise())
                is Result.Error -> Log.e("SLM", "Error occurred while getting exercise from local: ${exercise.error}")
            }
        }
        return exerciseList
    }

}
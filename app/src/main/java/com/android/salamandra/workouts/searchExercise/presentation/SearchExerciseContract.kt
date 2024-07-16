package com.android.salamandra.workouts.searchExercise.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.Exercise


data class SearchExerciseState(
    val loading: Boolean,
    val error: RootError?,
    val searchTerm: String,
    val searchResultExercises: List<Exercise>,
    val addedExercisesIds: Array<String>,
) : State {
    companion object {
        val initial: SearchExerciseState = SearchExerciseState(
            loading = false,
            error = null,
            searchTerm = "",
            searchResultExercises = emptyList(),
            addedExercisesIds = emptyArray(),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchExerciseState

        return addedExercisesIds.contentEquals(other.addedExercisesIds)
    }

    override fun hashCode(): Int {
        return addedExercisesIds.contentHashCode()
    }
}

sealed class SearchExerciseIntent : Intent {
    data class Error(val error: RootError) : SearchExerciseIntent()
    data object CloseError : SearchExerciseIntent()
    data class ChangeSearchTerm(val newTerm: String) : SearchExerciseIntent()
    data class AddExercise(val exercise: String) : SearchExerciseIntent()
    data object SearchExercise : SearchExerciseIntent()
    data object NavigateToEdit : SearchExerciseIntent()
}

sealed class SearchExerciseEvent : Event {
    data object NavigateToEdit : SearchExerciseEvent()
}
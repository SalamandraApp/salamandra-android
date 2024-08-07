package com.android.salamandra.workouts.search.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.Exercise


data class SearchState(
    val error: RootError?,
    val searchTerm: String,
    val searchResultExercises: List<Exercise>,
    val addedExercisesIds: List<String>,
    val selectedExercise: Exercise?
) : State {
    companion object {
        val initial: SearchState = SearchState(
            error = null,
            searchTerm = "",
            searchResultExercises = emptyList(),
            addedExercisesIds = emptyList(),
            selectedExercise = null
        )
    }


}

sealed class SearchIntent : Intent {
    data class Error(val error: RootError) : SearchIntent()

    data object CloseError : SearchIntent()

    data class ChangeSearchTerm(val newTerm: String) : SearchIntent()

    data class AddExercise(val exercise: Exercise) : SearchIntent()

    data object SearchExercise : SearchIntent()

    data object NavigateToEdit : SearchIntent()

    data object HideBottomSheet : SearchIntent()

    data class ShowBottomSheet(val exercise: Exercise) : SearchIntent()
}

sealed class SearchEvent : Event {
    data object NavigateToEdit : SearchEvent()
}

data class SearchNavArgs(
    val dummy: Int? = null
): NavArgs

package com.android.salamandra.workouts.search.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.Exercise


data class SearchState(
    val loading: Boolean,
    val error: RootError?,
    val searchTerm: String,
    val searchResultExercises: List<Exercise>,
    val addedExercisesIds: Array<String>,
    val bottomSheet: Boolean,
) : State {
    companion object {
        val initial: SearchState = SearchState(
            loading = false,
            error = null,
            searchTerm = "",
            searchResultExercises = emptyList(),
            addedExercisesIds = emptyArray(),
            bottomSheet = false,
        )
    }
}

sealed class SearchIntent: Intent {
    data class Error(val error: RootError): SearchIntent()
    data object CloseError: SearchIntent()
    data class ChangeSearchTerm(val newTerm: String) : SearchIntent()
    data class AddExercise(val exercise: String) : SearchIntent()
    data object SearchExercise : SearchIntent()
    data object NavigateToEdit : SearchIntent()
    data object ShowBottomSheet: SearchIntent()
    data object HideBottomSheet: SearchIntent()
}

sealed class SearchEvent: Event{
    data object NavigateToEdit : SearchEvent()
}

data class SearchNavArgs(
    val dummy: Int? = null
): NavArgs
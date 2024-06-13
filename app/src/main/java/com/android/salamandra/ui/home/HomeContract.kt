package com.android.salamandra.ui.home

import com.android.salamandra.core.boilerplate.Event
import com.android.salamandra.domain.model.Exercise
import com.android.salamandra.ui.UiText
import com.android.salamandra.core.boilerplate.Intent
import com.android.salamandra.core.boilerplate.State


data class HomeState(
    val loading: Boolean,
    val error: UiText?,
    val exList: List<Exercise>?
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = false,
            error = null,
            exList = null
        )
    }
}
sealed class HomeIntent: Intent {
    data class Loading(val isLoading: Boolean): HomeIntent()
    data class Error(val error: UiText): HomeIntent()
    data object CloseError: HomeIntent()
    data class SearchExercise(val term: String): HomeIntent()
}
sealed class HomeEvent: Event {
}
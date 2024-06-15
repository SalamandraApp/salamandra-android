package com.android.salamandra.home.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State


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
    data object Logout: HomeIntent()
}
sealed class HomeEvent: Event {
    data object Logout: HomeEvent()
}
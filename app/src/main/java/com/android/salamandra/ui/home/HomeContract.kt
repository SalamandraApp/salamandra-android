package com.android.salamandra.ui.home

import com.android.salamandra.domain.model.ExerciseModel
import com.android.salamandra.ui.UiText
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class HomeState(
    val loading: Boolean,
    val error: UiText?,
    val exList: List<ExerciseModel>?
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
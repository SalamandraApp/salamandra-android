package com.android.salamandra.home.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.util.WORKOUT_PREVIEW_LIST
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec


data class HomeState(
    val loading: Boolean,
    val error: RootError?,
    val wkPreviewList: List<WorkoutPreview>
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = false,
            error = null,
            wkPreviewList = emptyList()
        )
    }
}

sealed class HomeIntent : Intent {
    data class Loading(val isLoading: Boolean) : HomeIntent()
    data class Error(val error: RootError) : HomeIntent()
    data object CloseError : HomeIntent()
    data object Logout : HomeIntent()
    data object NewWk: HomeIntent()
    data class BottomBarClicked(val destination: DirectionDestinationSpec): HomeIntent()
}

sealed class HomeEvent : Event {
    data object Logout : HomeEvent()
    data object NavigateToEditWk: HomeEvent()
    data class BottomBarClicked(val destination: DirectionDestinationSpec): HomeEvent()
}
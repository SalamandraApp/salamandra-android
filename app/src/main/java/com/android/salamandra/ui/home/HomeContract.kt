package com.android.salamandra.ui.home

import com.android.salamandra.domain.model.ExerciseModel
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class HomeState(
    val loading: Boolean,
    val error: Error,
    val exList: List<ExerciseModel>?
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = false,
            error = Error(false, null),
            exList = null
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class HomeIntent: Intent {
    data object Loading: HomeIntent()
    data class Error(val errorMsg: String): HomeIntent()
    data class SetExList(val exList: List<ExerciseModel>): HomeIntent()
}
package com.android.salamandra.ui.home

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class HomeState(
    val loading: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class HomeIntent: Intent {
    data object Loading: HomeIntent()
    data class Error(val errorMsg: String): HomeIntent()
}
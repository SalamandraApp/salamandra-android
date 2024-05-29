package com.android.salamandra.core.boilerplate.template

import com.android.salamandra.ui.UiText
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class tState(
    val loading: Boolean,
    val error: UiText?
) : State {
    companion object {
        val initial: tState = tState(
            loading = false,
            error = null
        )
    }
}

sealed class tIntent: Intent {
    data class Loading(val isLoading: Boolean): tIntent()
    data class Error(val error: UiText): tIntent()
    data object CloseError: tIntent()
}
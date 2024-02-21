package com.android.salamandra.core.boilerplate.template

import com.android.salamandra.domain.model.UiError
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class tState(
    val loading: Boolean,
    val error: UiError
//    val counter: Int,
) : State {
    companion object {
        val initial: tState = tState(
            loading = false,
            error = UiError(false, null)
//            counter = 0,
        )
    }
}

sealed class tIntent: Intent {
    data class Loading(val isLoading: Boolean): tIntent()
    data class Error(val errorMsg: String): tIntent()
    data object CloseError: tIntent()
}
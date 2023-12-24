package com.android.salamandra.core.boilerplate.template

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class tState(
    val loading: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: tState = tState(
            loading = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class tIntent: Intent {
    data object Loading: tIntent()
    data class Error(val errorMsg: String): tIntent()
}
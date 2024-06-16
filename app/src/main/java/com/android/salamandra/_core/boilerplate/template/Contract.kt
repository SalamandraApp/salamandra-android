package com.android.salamandra._core.boilerplate.template

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State


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
    data class Error(val error: UiText): tIntent()
    data object CloseError: tIntent()
}

sealed class tEvent: Event{
}

data class tNavArgs(
    val dummy: Int? = null
): NavArgs